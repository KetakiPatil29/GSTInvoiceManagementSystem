package com.gstinvoice.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gstinvoice.Repository.CustomerRepository;
import com.gstinvoice.Repository.InvoiceRepository;
import com.gstinvoice.entity.Customer;
import com.gstinvoice.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class CustomerController {
	 private final CustomerService customerService;
	 @Autowired
	    private InvoiceRepository invoiceRepository;

	    @Autowired
	    public CustomerController(CustomerService customerService) {
	        this.customerService = customerService;
	    }
	    
	    @Autowired
	    private CustomerRepository customerRepository;
	    // Create a new Customer.
	    @Operation(
	    		 summary = "Create new Customer",
	    		 description = "")
	    @PostMapping("/customers")
	    public ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customer) {
	    	Customer newCustomer = customerService.saveCustomer(customer);
	        return ResponseEntity.ok(newCustomer);
	    }

	    // Get all Customer.
	     @Operation(
	    		 summary = "Fetach all Customer",
	    		 description = "")
	    @GetMapping("/customers")
	     public ResponseEntity<List<Customer>> getAllCustomers() {
	         List<Customer> customers = customerRepository.findAll();
	         return ResponseEntity.ok(customers);
	     }
	        

	    // Get a Customer by ID.
	     @Operation(
	    		 summary = "Fetach Customer by ID",
	    		 description = "")
	    @GetMapping("/customers/{id}")
	    public ResponseEntity<Customer> getcustomerById(@Valid @PathVariable Long id) {
	        Optional<Customer> customer = customerService.getCustomerById(id);
	        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    // Update a Customer by ID.
	     @Operation(
	    		 summary = " Update Customer by ID",
	    		 description = "")
	    @PutMapping("/customers/{id}")
	    public ResponseEntity<Customer> updateCustomer(@Valid @PathVariable Long id, @RequestBody Customer customer) {
	    	Customer updatedCustomer= customerService.updateCustomer(id, customer);
	        return ResponseEntity.ok(updatedCustomer);
	    }

	    // Delete a Customer by ID.
	     @Operation(
	    		 summary = "Delete Customer by ID",
	    		 description = "")
	    @DeleteMapping("/customers/{id}")
	    public ResponseEntity<String> deleteCustomer(@Valid @PathVariable Long id) {
	    	customerService.deleteCustomer(id);
	        return ResponseEntity.ok("Customer deleted successfully");
	    }
	     
	     @GetMapping("customers/{id}/can-delete")
	     public ResponseEntity<Boolean> canDeleteCustomer(@Valid @PathVariable Long id) {
	         boolean hasInvoices = invoiceRepository.existsByCustomerId(id);
	         return ResponseEntity.ok(!hasInvoices); // true if deletable
	     }

}
