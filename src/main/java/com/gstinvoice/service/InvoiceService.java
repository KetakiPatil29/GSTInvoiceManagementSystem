package com.gstinvoice.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.gstinvoice.Repository.CustomerRepository;
import com.gstinvoice.Repository.InvoiceItemRepository;
import com.gstinvoice.Repository.InvoiceRepository;
import com.gstinvoice.Repository.ProductRepository;
import com.gstinvoice.entity.Customer;
import com.gstinvoice.entity.Invoice;
import com.gstinvoice.entity.InvoiceItems;
import com.gstinvoice.entity.Product;
import com.gstinvoice.model.request.CreateInvoiceRequest;
import com.gstinvoice.model.request.ProductRequest;
import com.gstinvoice.model.request.CreateInvoiceRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Service
public class InvoiceService {

	private final InvoiceItemRepository invoiceItemRepository;
	private final InvoiceRepository invoiceRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	public InvoiceService(InvoiceRepository invoiceRepository, InvoiceItemRepository invoiceItemRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceItemRepository = invoiceItemRepository;
	}

	// Save a Invoice

	public Invoice saveInvoice(CreateInvoiceRequest createInvoiceRequest) {
//		// Fetch customer
		Customer customer = customerRepository.findById(createInvoiceRequest.getCustomer_id())
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Create invoice
		Invoice newInvoice = new Invoice();
		newInvoice.setCustomer(customer);
		
		// newInvoice.setId(createInvoiceRequest.getCustomer_id());
		newInvoice.setStatus(createInvoiceRequest.getStatus());
		newInvoice.setDate(createInvoiceRequest.getInvoiceDate());
		
		List<InvoiceItems> newInvoiceItemArray = new ArrayList<InvoiceItems>();
	
		for (ProductRequest item : createInvoiceRequest.getItems()) {
			Product product = productRepository.findById(item.getProduct_id())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			InvoiceItems newInvoiceItemObject = new InvoiceItems();

			newInvoiceItemObject.setProduct(product); // ✅ This links the product

			// newInvoiceItemObject.setId(item.getProduct_id()); //Error - detached entity
			// passed to persist: com.gstinvoice.entity.InvoiceItems
			newInvoiceItemObject.setQuantity(item.getQuantity());
			newInvoiceItemObject.setRate(item.getRate());
			newInvoiceItemObject.setAmount(item.getAmount() * item.getQuantity());
			
			newInvoiceItemObject.setInvoice(newInvoice); // ✅ Link to parent
			newInvoiceItemArray.add(newInvoiceItemObject);
		}

		newInvoice.setItems(newInvoiceItemArray);
		
		List<InvoiceItems> items = newInvoiceItemArray;
		newInvoice.setTotal_amount(calculateTotalAmount(items));
		newInvoice.setCgst(calculateCgst(items, customer.getState()));
		newInvoice.setSgst(calculateSgst(items, customer.getState()));
		newInvoice.setIgst(calculateIgst(items, customer.getState()));
		newInvoice.setGrand_total(calculateGrandTotal(items, customer.getState()));
		
		// ✅ Save invoice first to get ID
		Invoice savedInvoice = invoiceRepository.save(newInvoice);

		// ✅ Generate invoice number This gives you invoice numbers like: INV-00001,
		// INV-00002, etc.
		String invoiceNumber = "INV-" + String.format("%05d", savedInvoice.getId());
		savedInvoice.setInvoice_number(invoiceNumber);
		invoiceRepository.save(savedInvoice); // ✅ second save updates the number

		return invoiceRepository.save(newInvoice);
	}

	// Get all the Invoice.

	public List<Invoice> getAllInvoice() {
		return invoiceRepository.findAll();
	}
	// Get one Invoice by ID.

	public Optional<Invoice> getInvoiceById(Long id) {
		return invoiceRepository.findById(id);
	}

	// Update a Invoice

	public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
		Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
		if (existingInvoice.isPresent()) {
			Invoice invoice = existingInvoice.get();
			invoice.setInvoice_number(updatedInvoice.getInvoice_number());
			invoice.setCustomer(updatedInvoice.getCustomer());
			invoice.setDate(updatedInvoice.getDate());
			invoice.setTotal_amount(updatedInvoice.getTotal_amount());
			invoice.setStatus(updatedInvoice.getStatus());
			return invoiceRepository.save(invoice);
		} else {
			throw new RuntimeException("Invoice not found");
		}
	}
	// Delete the Invoice by ID.

	public void deleteInvoice(Long id) {
		invoiceRepository.deleteById(id);
	}

	@Autowired
	private ProductRepository productRepository;

	public Invoice addProductToInvoice(Long invoiceId, Long productId, int quantity) {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new RuntimeException("Invoice not found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		InvoiceItems item = new InvoiceItems();
		item.setInvoice(invoice);
		item.setProduct(product);
		item.setQuantity(quantity);
		item.setRate(product.getGst_rate());
		item.setAmount(product.getUnit_price() * quantity);

		invoice.getItems().add(item);
		return invoiceRepository.save(invoice);
	}

// Get a Invoice by ID pdf   

	public byte[] generateInvoicePdf(Long invoiceId) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();

			// Fetch invoice with customer and items
			Invoice invoice = invoiceRepository.findById(invoiceId)
					.orElseThrow(() -> new RuntimeException("Invoice not found"));

			Customer customer = invoice.getCustomer();
			List<InvoiceItems> items = invoice.getItems();

			String CompanyState = " Maharashtra";

			// Header
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
			Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

			Font bold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
			Font regular = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

			Paragraph title = new Paragraph("TAX INVOICE", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);

			Paragraph t = new Paragraph();
			t.add(new Chunk("Bright Traders" + "\n", bold));
			t.add(new Chunk("123, ABC Building, DEF Street, GHI " + "\n", regular));
			t.add(new Chunk("GSTIN : ", bold));
			t.add(new Chunk(String.valueOf("22-AAAAA0000A1-Z-5") + "\n", regular));
			t.add(new Chunk("Location : ", bold));
			t.add(new Chunk(String.valueOf(CompanyState) + "\n", regular));
			t.setLeading(20f);
			document.add(t);

			// Invoice Info
			Paragraph n = new Paragraph();
			n.add(new Chunk("Invoice No : ", bold));
			n.add(new Chunk(String.valueOf(invoice.getInvoice_number()) + "\n", regular));
			n.add(new Chunk(" Invoice Date : ", bold));
			n.add(new Chunk(String.valueOf(invoice.getDate()) + "\n", regular));

			n.setAlignment(Element.ALIGN_RIGHT);
			n.setLeading(20f);
			document.add(n);

			// Customer Info

			Paragraph c = new Paragraph();
			c.add(new Chunk("Customer Id : ", bold));
			c.add(new Chunk(String.valueOf(customer.getId()) + "\n", regular));
			c.add(new Chunk("Name : ", bold));
			c.add(new Chunk(String.valueOf(customer.getName()) + "\n", regular));
			c.add(new Chunk("Address : ", bold));
			c.add(new Chunk(String.valueOf(customer.getAddress()) + "\n", regular));
			c.add(new Chunk("Location : ", bold));
			c.add(new Chunk(String.valueOf(customer.getState()) + "\n", regular));
			c.setLeading(20f);
			document.add(c);

			Paragraph p = new Paragraph();
			p.add(new Chunk("GSTIN : ", bold));
			p.add(new Chunk(String.valueOf(customer.getGstin()) + "\n", regular));
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			document.add(new Paragraph("\n")); // adds a blank line

			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
			BaseColor rowBg = new BaseColor(230, 230, 230); // Light gray

			// Table
			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			float[] columnWidths = {1f, 3f, 2f, 1f, 2f, 2f}; // Product Name column is 3x wider
			table.setWidths(columnWidths);
			table.setSpacingBefore(10);
			String[] headers = { "Sr.No", "Product Name & HSN Code", "Price",
					"Qty", "GST%", "Amount" };

			for (String header : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
				cell.setBackgroundColor(rowBg); // ✅ Apply to cell, not table
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(8);
				table.addCell(cell);
			}

			int srNo = 1; // Start from 1
			for (InvoiceItems item : invoice.getItems()) {
				Product product = item.getProduct();
				double totalAmount = product.getUnit_price() * item.getQuantity();

				table.addCell(String.valueOf(srNo++));
				table.addCell(product.getName() + "\n" + product.getHsn_code());
				table.addCell(String.valueOf(product.getUnit_price()));
				table.addCell(String.valueOf(item.getQuantity()));
				table.addCell(String.valueOf(product.getGst_rate() + "%")); // or item.getGstPercent()
				table.addCell("₹" + totalAmount);
			}
			PdfPCell mergedCell = new PdfPCell(new Paragraph());
			mergedCell.setColspan(4);
			table.addCell(mergedCell);
			PdfPCell labelCell = new PdfPCell(new Phrase("Total", bold));
			// labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(labelCell);
			table.addCell(String.valueOf(String.valueOf("₹" + calculateTotalAmount(items))));

			PdfPCell mergedCell1 = new PdfPCell(new Paragraph("Remarks :", bold));
			// mergedCell1.setRowspan(3);
			mergedCell1.setColspan(4);
			table.addCell(mergedCell1);

			PdfPCell mergedCell2 = new PdfPCell(new Paragraph());
			mergedCell2.setColspan(5);
			table.addCell(mergedCell2);

			PdfPCell mergedCell3 = new PdfPCell(new Paragraph());
			mergedCell3.setColspan(4);
			table.addCell(mergedCell3);
			PdfPCell mergedCell4 = new PdfPCell(new Paragraph("Summary", bold));
			mergedCell4.setColspan(2);
			mergedCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(mergedCell4);

			if (calculateCgst(items, customer.getState()) != 0 || calculateSgst(items, customer.getState()) != 0) {
				PdfPCell mergedCell5 = new PdfPCell(new Paragraph());
				mergedCell5.setColspan(4);
				table.addCell(mergedCell5);
				PdfPCell mergedCell6 = new PdfPCell(new Paragraph("CGST", bold));
				mergedCell6.setColspan(1);
				mergedCell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(mergedCell6);
				PdfPCell mergedCell7 = new PdfPCell(
						new Paragraph(String.valueOf(calculateCgst(items, customer.getState()))));
				mergedCell7.setColspan(1);
				mergedCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(mergedCell7);

				PdfPCell mergedCell8 = new PdfPCell(new Paragraph());
				mergedCell8.setColspan(4);
				table.addCell(mergedCell8);
				PdfPCell mergedCell9 = new PdfPCell(new Paragraph("SGST", bold));
				mergedCell9.setColspan(1);
				mergedCell9.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(mergedCell9);
				PdfPCell mergedCell10 = new PdfPCell(
						new Paragraph(String.valueOf(calculateSgst(items, customer.getState()))));
				mergedCell10.setColspan(1);
				mergedCell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(mergedCell10);
			}

			if (calculateIgst(items, customer.getState()) != 0) {
				PdfPCell mergedCell11 = new PdfPCell(new Paragraph());
				mergedCell11.setColspan(4);
				table.addCell(mergedCell11);
				PdfPCell mergedCell12 = new PdfPCell(new Paragraph("IGST", bold));
				mergedCell12.setColspan(1);
				mergedCell12.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(mergedCell12);
				PdfPCell mergedCell13 = new PdfPCell(
						new Paragraph(String.valueOf(calculateIgst(items, customer.getState()))));
				mergedCell13.setColspan(1);
				mergedCell13.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(mergedCell13);
			}

			PdfPCell mergedCell14 = new PdfPCell(new Paragraph());
			mergedCell14.setColspan(4);
			table.addCell(mergedCell14);
			PdfPCell mergedCell15 = new PdfPCell(new Paragraph("Grand Total", bold));
			mergedCell15.setColspan(1);
			mergedCell15.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(mergedCell15);
			PdfPCell mergedCell16 = new PdfPCell(
					new Paragraph(String.valueOf("₹" + calculateGrandTotal(items, customer.getState()))));
			mergedCell16.setColspan(1);
			mergedCell16.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(mergedCell16);
			document.add(table);
			// document.add(new Paragraph("\n")); // adds a blank line

			Paragraph signature = new Paragraph("Accountant's Signature : ____________________", normalFont);
			signature.setSpacingBefore(40);
			document.add(signature);

			Paragraph footer = new Paragraph("Thank You. Visit Again.", sectionFont);
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.setSpacingBefore(50);
			document.add(footer);

			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generating invoice PDF", e);
		}
	}

	public double calculateTotalAmount(List<InvoiceItems> items) {
		double total = 0.0;
		for (InvoiceItems item : items) {
			double price = item.getProduct().getUnit_price(); // from Product
			long quantity = item.getQuantity(); // from InvoiceItems
			total += price * quantity;
		}
		return total;
	}

	String CompanyState = "Maharashtra";

	public double calculateCgst(List<InvoiceItems> items, String CustomerState) {
		double CGST = 0.0;
		if (CompanyState.equals(CustomerState)) {
			for (InvoiceItems item : items) {
				Product product = item.getProduct();
				double totalAmount = product.getUnit_price() * item.getQuantity();
				double gstAmount = totalAmount * product.getGst_rate() / 100;
				CGST += gstAmount / 2;
			}
		}
		return CGST;
	}

	public double calculateSgst(List<InvoiceItems> items, String CustomerState) {
		double SGST = 0.0;
		if (CompanyState.equals(CustomerState)) {
			for (InvoiceItems item : items) {
				Product product = item.getProduct();
				double totalAmount = product.getUnit_price() * item.getQuantity();
				double gstAmount = totalAmount * product.getGst_rate() / 100;
				SGST += gstAmount / 2;
			}
		}
		return SGST;
	}

	public double calculateIgst(List<InvoiceItems> items, String CustomerState) {
		double IGST = 0.0;
		if (!CompanyState.equals(CustomerState)) {
			for (InvoiceItems item : items) {
				Product product = item.getProduct();
				double totalAmount = product.getUnit_price() * item.getQuantity();
				double gstAmount = totalAmount * product.getGst_rate() / 100;
				IGST += gstAmount;
			}
		}
		return IGST;
	}

	public double calculateGrandTotal(List<InvoiceItems> items, String CustomerState) {

		double baseAmount = calculateTotalAmount(items);
		double gstAmount = calculateCgst(items, CustomerState) + calculateSgst(items, CustomerState)
				+ calculateIgst(items, CustomerState);
		double totalAmount = baseAmount + gstAmount;

		return totalAmount;
	}
}
