package com.petshop.petshop.controller;

import com.petshop.petshop.model.Customer;
import com.petshop.petshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok().body(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Customer> getCustomerByName(@RequestParam String name) {
        Optional<Customer> customer = customerRepository.findByName(name);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok().body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(customerDetails.getName());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setPhone(customerDetails.getPhone());
                    Customer updatedCustomer = customerRepository.save(customer);
                    return ResponseEntity.ok().body(updatedCustomer);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCustomerByName(@RequestParam String name) {
        Optional<Customer> customer = customerRepository.findByName(name);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
            return ResponseEntity.ok().<Void>build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}