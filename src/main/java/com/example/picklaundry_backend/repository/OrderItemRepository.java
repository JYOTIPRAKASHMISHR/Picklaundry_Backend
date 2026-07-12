package com.example.picklaundry_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.picklaundry_backend.entity.Order;
import com.example.picklaundry_backend.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get all items of an order
    List<OrderItem> findByOrder(Order order);
    // List<OrderItem> findByOrderOrderId(String orderId);

}