package com.gstinvoice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gstinvoice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
