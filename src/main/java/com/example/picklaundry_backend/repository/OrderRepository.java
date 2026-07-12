package com.example.picklaundry_backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.picklaundry_backend.entity.Order;
import com.example.picklaundry_backend.entity.User;
import com.example.picklaundry_backend.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by generated Order ID
    Optional<Order> findByOrderId(String orderId);

    // Find all orders of a particular user
    List<Order> findByUser(User user);

    // Latest orders first
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByStatusIn(List<OrderStatus> statuses);

   @Query("""
SELECT COALESCE(SUM(o.grandTotal), 0)
FROM Order o
WHERE o.status = :status
""")
BigDecimal getRevenueByStatus(@Param("status") OrderStatus status);

long countByStatus(OrderStatus status);

    // List<Order> findByCustomer(User customer);

}