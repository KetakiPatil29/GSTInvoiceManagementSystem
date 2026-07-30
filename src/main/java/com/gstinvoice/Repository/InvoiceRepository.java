package com.gstinvoice.Repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gstinvoice.entity.Invoice;
import com.gstinvoice.model.request.CreateInvoiceRequest;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByCustomerId(Long customerId);
    
}

