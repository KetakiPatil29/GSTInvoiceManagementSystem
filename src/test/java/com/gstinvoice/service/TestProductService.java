package com.gstinvoice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gstinvoice.Repository.ProductRepository;
import com.gstinvoice.entity.Customer;
import com.gstinvoice.entity.Product;

@Service
public class TestProductService {
	private final ProductRepository productRepository;

	@Autowired
	public TestProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	// Save a product

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}
	// Get all the products.

	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}
	
	// Update a Product

		public Product updateProduct(Long id, Product updatedProduct) {
			Optional<Product> existingProduct = productRepository.findById(id);
			if (existingProduct.isPresent()) {
				Product product = existingProduct.get();
				product.setName(updatedProduct.getName());
				product.setDescription(updatedProduct.getDescription());
				product.setHsn_code(updatedProduct.getHsn_code());
				product.setGst_rate(updatedProduct.getGst_rate());
				product.setUnit_price(updatedProduct.getUnit_price());
				
				return productRepository.save(product);
			} else {
				throw new RuntimeException("Product not found");
			}
		}
		// Delete the Product by ID.

		public void deleteProduct(Long id) {
			productRepository.deleteById(id);
		}
}
