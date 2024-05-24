package com.petshop.petshop.repository;

import com.petshop.petshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDate(String orderDate);
}
