package com.example.picklaundry_backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Parent Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Laundry Service
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private LaundryService service;

    // Service Item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_item_id", nullable = false)
    private ServiceItem serviceItem;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String selectedPlan;

    @Column(nullable = false)
    private Integer quantity;

    // Selected plan price
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // quantity × price
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}