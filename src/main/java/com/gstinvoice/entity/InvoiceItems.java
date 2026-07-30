package com.gstinvoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Entity
@Table(name = "invoice_items")
@Data
public class InvoiceItems {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "invoice_id")
	@JsonIgnore
	private Invoice invoice;
	
	 @ManyToOne
	 @JoinColumn(name = "product_id")
	 private Product product;
	 
   // @Column(nullable = false, insertable = false, updatable = false)
    //private long invoice_id;

//    @Column(nullable = false, insertable = false, updatable = false)
//    private long product_id;

	  
    @Column(nullable = false)
    @Positive(message = "Quantity must be greater than zero for all selected products.")
    private long quantity;
    
    @Column(nullable = false)
    @Positive(message = "Rate must be greater than zero for all selected products.")
    private long rate;
    
    @Column(nullable = false)
    @Positive(message = "Amount must be greater than zero for all selected products.")
    private long amount;

//	@Column(nullable = false)
//	@Positive(message = "Amount must be greater than zero for all selected products.")
//	private long discount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

//	public long getInvoice_id() {
//		return invoice_id;
//	}
//
//	public void setInvoice_id(long invoice_id) {
//		this.invoice_id = invoice_id;
//	}

//	public long getProduct_id() {
//		return product_id;
//	}
//
//	public void setProduct_id(long product_id) {
//		this.product_id = product_id;
//	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

//	public long getDiscount() {
//		return discount;
//	}
//
//	public void setDiscount(long discount) {
//		this.discount = discount;
//	}
}
