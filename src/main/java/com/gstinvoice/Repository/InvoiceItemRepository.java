package com.gstinvoice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gstinvoice.entity.InvoiceItems;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItems, Long> {
	boolean existsByProductId(Long productId);

}
