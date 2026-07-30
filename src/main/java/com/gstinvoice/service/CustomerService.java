package com.gstinvoice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gstinvoice.Repository.CustomerRepository;
import com.gstinvoice.entity.Customer;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	// Save a Customer

	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	// Get all the Customer.

	public List<Customer> getAllCustomer() {
		return customerRepository.findAll();
	}
	
	// Get one Customer by ID.

	public Optional<Customer> getCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	// Update a Customer

	public Customer updateCustomer(Long id, Customer updatedCustomer) {
		Optional<Customer> existingCustomer = customerRepository.findById(id);
		if (existingCustomer.isPresent()) {
			Customer customer = existingCustomer.get();
			customer.setName(updatedCustomer.getName());
			customer.setGstin(updatedCustomer.getGstin());
			customer.setState(updatedCustomer.getState());
			customer.setEmail(updatedCustomer.getEmail());
			customer.setPhone(updatedCustomer.getPhone());
			customer.setAddress(updatedCustomer.getAddress());
			return customerRepository.save(customer);
		} else {
			throw new RuntimeException("Customer not found");
		}
	}
	// Delete the Customer by ID.

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}
}
