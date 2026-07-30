package com.gstinvoice.model.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gstinvoice.entity.Customer;
import com.gstinvoice.entity.Product;

public class CreateInvoiceRequest {

	 private Long customer_id; 
	 
	 private List<ProductRequest> items;
	 
	 private LocalDate invoiceDate;
	 
	 private long cgst;
	 
	 private long sgst;
	 
	 private long igst;
	 
	 private double grand_total;
	 
	 private String status;

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customerId) {
		this.customer_id = customerId;
	}

	public List<ProductRequest> getItems() {
		return items;
	}

	public void setItems(List<ProductRequest> items) {
		this.items = items;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public long getCgst() {
		return cgst;
	}

	public void setCgst(long cgst) {
		this.cgst = cgst;
	}

	
	public long getSgst() {
		return sgst;
	}

	public void setSgst(long sgst) {
		this.sgst = sgst;
	}

	public long getIgst() {
		return igst;
	}

	public void setIgst(long igst) {
		this.igst = igst;
	}

	public double getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(double grand_total) {
		this.grand_total = grand_total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	 
	 
	    

}
