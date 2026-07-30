package com.gstinvoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{2}[A-Z]{10}[0-9]{1}[A-Z]{1}[0-9A-Z]{1}",
    message = "GstIn number must be 15 digits and letters")
    private String gstin;

    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Column(nullable = false)
    @Pattern(regexp = "\\d{10}",
    message = "Phone number must be ten digits")
    private String phone;
    
    @Column(nullable = false)
    private String address;

    // Constructors, getters and setters, and other methods...

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGstin() {
        return gstin;
    }

    public String getState() {
        return state;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getAddress() {
        return address;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
}
