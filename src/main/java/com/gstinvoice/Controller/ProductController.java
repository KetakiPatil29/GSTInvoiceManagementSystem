package com.gstinvoice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gstinvoice.Repository.InvoiceItemRepository;
import com.gstinvoice.entity.Customer;
import com.gstinvoice.entity.Product;
import com.gstinvoice.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
	
@RequestMapping("/api")
public class ProductController {
	private final ProductService productService;
	 @Autowired
	private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product.
    @Operation(
    		 summary = "Create new Product",
    		 description = "")
    @PostMapping("/products")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) {
    	Product newProduct = productService.saveProduct(product);
        return ResponseEntity.ok(newProduct);
    }

    // Get all products.
     @Operation(
    		 summary = "Fetach all Product",
    		 description = "")
    @GetMapping("/products")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();    
    }
     
     // Update a Product by ID.
     @Operation(
    		 summary = " Update Poducts by ID",
    		 description = "")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@Valid @PathVariable Long id, @RequestBody Product product) {
    	 Product updatedProduct= productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a Customer by ID.
     @Operation(
    		 summary = "Delete Product by ID",
    		 description = "")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@Valid @PathVariable Long id) {
    	 productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
     
     @GetMapping("/products/{id}/can-delete")
     public ResponseEntity<Boolean> canDeleteProduct(@Valid @PathVariable Long id) {
         boolean isLinkedToInvoice = invoiceItemRepository.existsByProductId(id);
         return ResponseEntity.ok(!isLinkedToInvoice); // true means deletable
     }


}
