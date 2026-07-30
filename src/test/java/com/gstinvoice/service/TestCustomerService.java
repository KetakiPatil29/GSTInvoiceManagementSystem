package com.gstinvoice.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gstinvoice.Repository.CustomerRepository;
import com.gstinvoice.entity.Customer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestCustomerService {

	@Mock
	private CustomerRepository customerRepository;
	
	 @InjectMocks
	 private CustomerService customerservice;
	 
	 @Mock
	 private Customer customer;
	 
	 @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	
	 @Test
	void TestsaveCustomer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("Ketaki");
		customer.setGstin("2345678io");
		customer.setState("India");
		customer.setEmail("k@gmail.com");
		customer.setPhone("12345678");
		customer.setAddress("Pune");
		
		assertEquals(1,customer.getId());
		 assertEquals("Ketaki",customer.getName());
		 assertEquals("2345678io",customer.getGstin());
		 assertEquals("India",customer.getState());
		 assertEquals("k@gmail.com",customer.getEmail());
		 assertEquals("12345678",customer.getPhone());
		 assertEquals("Pune",customer.getAddress());
	}
	 
	 @Test
	 void TestgetAllCustomer() {
		 Customer customer = new Customer();
	        customer.setId(1L);
	        customer.setName("Ketaki");
	        customer.setGstin("2345678io");
	        customer.setState("India");
	        customer.setEmail("k@gmail.com");
	        customer.setPhone("12345678");
	        customer.setAddress("Pune");

	        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
	        List<Customer> result = customerservice.getAllCustomer();

	        assertEquals(1, result.size());
	        assertEquals("Ketaki", result.get(0).getName());
	 }
	 
	 @Test
	 void TestgetCustomerById() {
		 Long customerId = 1L;
	        Customer customer = new Customer();
	        customer.setId(customerId);
	        customer.setName("Ketaki");
	        customer.setGstin("2345678io");
	        customer.setState("India");
	        customer.setEmail("k@gmail.com");
	        customer.setPhone("12345678");
	        customer.setAddress("Pune");

	        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
	        // Act
	        Optional<Customer> result = customerservice.getCustomerById(customerId);

	        assertTrue(result.isPresent());
	        assertEquals("Ketaki", result.get().getName());
	        assertEquals("Pune", result.get().getAddress());
	   
	 }
	 
	 @Test
	    void testUpdateCustomer_Success() {
	        Long customerId = 1L;

	        Customer existingCustomer = new Customer();
	        existingCustomer.setId(customerId);
	        existingCustomer.setName("Old Name");

	        Customer updatedCustomer = new Customer();
	        updatedCustomer.setName("Ketaki");
	        updatedCustomer.setGstin("2345678io");
	        updatedCustomer.setState("India");
	        updatedCustomer.setEmail("k@gmail.com");
	        updatedCustomer.setPhone("12345678");
	        updatedCustomer.setAddress("Pune");

	        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
	        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

	        Customer result = customerservice.updateCustomer(customerId, updatedCustomer);

	        assertEquals("Ketaki", result.getName());
	        assertEquals("Pune", result.getAddress());
	    }

	    @Test
	    void testUpdateCustomer_NotFound() {
	        Long customerId = 99L;
	        Customer updatedCustomer = new Customer();

	        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

	        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	            customerservice.updateCustomer(customerId, updatedCustomer);
	        });

	        assertEquals("Customer not found", exception.getMessage());
	    }
	 
	 @Test
	 void TestdeleteCustomer() {
		 Long customerId = 1L;

	        // Act
	        customerservice.deleteCustomer(customerId);

	        // Assert
	        verify(customerRepository).deleteById(customerId);

	 }
}
