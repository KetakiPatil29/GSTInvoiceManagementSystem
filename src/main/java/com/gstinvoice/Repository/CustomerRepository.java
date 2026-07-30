package com.gstinvoice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gstinvoice.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}