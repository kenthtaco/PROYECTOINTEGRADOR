package com.petshop.petshop.controller;

import com.petshop.petshop.model.Order;
import com.petshop.petshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Order>> getOrdersByDate(@RequestParam String orderDate) {
        List<Order> orders = orderRepository.findByOrderDate(orderDate);
        if (!orders.isEmpty()) {
            return ResponseEntity.ok().body(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderDate(orderDetails.getOrderDate());
                    order.setCustomer(orderDetails.getCustomer());
                    order.setProducts(orderDetails.getProducts());
                    Order updatedOrder = orderRepository.save(order);
                    return ResponseEntity.ok().body(updatedOrder);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOrdersByDate(@RequestParam String orderDate) {
        List<Order> orders = orderRepository.findByOrderDate(orderDate);
        if (!orders.isEmpty()) {
            orderRepository.deleteAll(orders);
            return ResponseEntity.ok().<Void>build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
