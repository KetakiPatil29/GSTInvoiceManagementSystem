package com.gstinvoice.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
	
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItems> items;
 
    @Column(nullable = true)
    private String invoice_number;

//    @Column(name = "customer_id", insertable = false, updatable = false)
//    private Long customerId;

    @JsonProperty("invoiceDate")
    @Column(nullable = false)
    private LocalDate date;

    
    @Column(nullable = false)
    @Positive(message = "Total Amount must be greater than zero for all selected products.")
    private double total_amount;
    
    @Column(nullable = false)
    private double cgst ;
    
    @Column(nullable = false)
    private double sgst;
    
    @Column(nullable = false)
    private double igst;
    
    @Column(nullable = false)
    private double grand_total;
    

	@Column(nullable = false)
    private String status;
    
	//private long customer_id;  /////////////

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<InvoiceItems> getItems() {
		return items;
	}

	public void setItems(List<InvoiceItems> items) {
		this.items = items;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

//	public Long getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Long customerId) {
//		this.customerId = customerId;
//	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double totalAmount) {
		this.total_amount = totalAmount;
	}

	public double getCgst() {
		return cgst;
	}

	public void setCgst(double cgst) {
		this.cgst = cgst;
	}

	public double getSgst() {
		return sgst;
	}

	public void setSgst(double sgst) {
		this.sgst = sgst;
	}

	public double getIgst() {
		return igst;
	}

	public void setIgst(double igst) {
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

	public List<Product> getProducts() {
	    return products;
	}

	public void setProducts(List<Product> products) {
	    this.products = products;
	}
    

    // Constructors, getters and setters, and other methods...

    // Getters
 
}
