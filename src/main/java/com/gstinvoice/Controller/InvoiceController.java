package com.gstinvoice.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gstinvoice.Repository.InvoiceRepository;
import com.gstinvoice.entity.Invoice;
import com.gstinvoice.entity.InvoiceItems;
import com.gstinvoice.entity.Product;
import com.gstinvoice.model.request.CreateInvoiceRequest;
import com.gstinvoice.model.request.ProductRequest;
import com.gstinvoice.model.response.CreateInvoiceResponse;
import com.gstinvoice.service.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class InvoiceController {
	private final InvoiceService invoiceService;
	// private InvoiceRepository invoiceRepository;

	@Autowired
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

	// Create a new Invoice.
	@Operation(summary = "Create new Invoice", description = "")
	@PostMapping("/invoices")
	public ResponseEntity<CreateInvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest createInvoiceRequest) {

		System.out.println("Customer id - " + createInvoiceRequest.getCustomer_id());
		System.out.println("Invoice Date - " + createInvoiceRequest.getInvoiceDate());
		System.out.println("Invoice status - " + createInvoiceRequest.getStatus());
		

		for (ProductRequest item : createInvoiceRequest.getItems()) {
			System.out.println("Product id - " + item.getProduct_id());
			System.out.println("Amount - " + item.getAmount());
			System.out.println("Rate - " + item.getRate());
		}
		
		System.out.println("CGST: " + createInvoiceRequest.getCgst());
		System.out.println("SGST: " + createInvoiceRequest.getSgst());
		System.out.println("IGST: " + createInvoiceRequest.getIgst());
		System.out.println("Grand Total: " + createInvoiceRequest.getGrand_total());


		// Save invoice
		Invoice savedInvoice = invoiceService.saveInvoice(createInvoiceRequest);

		// Generate PDF (optional)
		// pdfService.generateInvoicePdf(savedInvoice.getId());

		// Return response with invoice ID and number
		CreateInvoiceResponse response = new CreateInvoiceResponse(savedInvoice.getId(),
				savedInvoice.getInvoice_number());

		return ResponseEntity.ok(response);
	}

	// Get all Invoice.
	@Operation(summary = "Fetach all Invoice", description = "")
	@GetMapping("/invoices")
	public List<Invoice> getAllInvoice() {
		return invoiceService.getAllInvoice();

	}

	// Get a Invoice by ID.
	@Operation(summary = "Fetach Customer by ID", description = "")
	@GetMapping("/invoices/{id}")
	public ResponseEntity<Invoice> getinvoiceById(@Valid @PathVariable Long id) {
		Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
		return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Update a Invoice by ID.
	@Operation(summary = " Update Invoice by ID", description = "")
	@PutMapping("/invoices/{id}")
	public ResponseEntity<Invoice> updateInvoice(@Valid @PathVariable Long id, @RequestBody Invoice invoice) {
		Invoice updatedInvoice = invoiceService.updateInvoice(id, invoice);
		return ResponseEntity.ok(updatedInvoice);
	}

	// Delete a Invoice by ID.
	@Operation(summary = "Delete Invoice by ID", description = "")
	@DeleteMapping("/invoices/{id}")
	public ResponseEntity<String> deleteInvoice(@Valid @PathVariable Long id) {
		invoiceService.deleteInvoice(id);
		return ResponseEntity.ok("Invoice deleted successfully");
	}

	// Get a Invoice by ID pdf.
	@GetMapping("/invoices/{id}/pdf")
	public ResponseEntity<byte[]> getInvoicePdf(@Valid @PathVariable Long id) {
		byte[] pdfBytes = invoiceService.generateInvoicePdf(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.builder("inline").filename("invoice_" + id + ".pdf").build());

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}
}
