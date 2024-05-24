package com.petshop.petshop.controller;

import com.petshop.petshop.model.Product;
import com.petshop.petshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductsByName(@RequestParam String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (!products.isEmpty()) {
            return ResponseEntity.ok().body(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok().body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setStock(productDetails.getStock());
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok().body(updatedProduct);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProductsByName(@RequestParam String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (!products.isEmpty()) {
            productRepository.deleteAll(products);
            return ResponseEntity.ok().<Void>build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
