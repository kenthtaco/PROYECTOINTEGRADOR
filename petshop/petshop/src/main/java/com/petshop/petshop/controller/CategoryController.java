package com.petshop.petshop.controller;

import com.petshop.petshop.model.Category;
import com.petshop.petshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Category> getCategoryByName(@RequestParam String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok().body(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    Category updatedCategory = categoryRepository.save(category);
                    return ResponseEntity.ok().body(updatedCategory);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCategoryByName(@RequestParam String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return ResponseEntity.ok().<Void>build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}