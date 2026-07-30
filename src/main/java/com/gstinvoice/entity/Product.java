package com.gstinvoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Entity
@Table(name = "products")
@Data
public class Product {
	
	@ManyToOne
	@JoinColumn(name = "invoice_id")
	private Invoice invoice;

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private long hsn_code;
    
    @Column(nullable = false)
    @Positive(message = "Gst Rate must be greater than zero for all selected products.")
    private long gst_rate;
    
    @Column(nullable = false)
    @Positive(message = "Price must be greater than zero for all selected products.")
    private long unit_price;

	
    

    // Constructors, getters and setters, and other methods...

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getHsn_code() {
        return hsn_code;
    }
    
    public long getGst_rate() {
        return gst_rate;
    }
    
    public long getUnit_price() {
        return unit_price;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHsn_code(long hsn_code) {
        this.hsn_code = hsn_code;
    }
    
    public void setGst_rate(long gst_rate) {
        this.gst_rate = gst_rate;
    }

    public void setUnit_price(Long unit_price) {
        this.unit_price = unit_price;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

}
