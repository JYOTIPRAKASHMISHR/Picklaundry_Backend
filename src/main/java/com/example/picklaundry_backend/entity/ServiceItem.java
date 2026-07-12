package com.example.picklaundry_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Shirt
    // Pant
    // Saree

    @Column(nullable = false)
    private String itemName;

    // Base Price

    @Column(nullable = false)
    private BigDecimal basePrice;

    // Express Price

    @Column(nullable = false)
    private BigDecimal expressPrice;

    // Premium Price

    @Column(nullable = false)
    private BigDecimal premiumPrice;

    // GST Percentage



    // Active/Inactive

    private boolean active = true;

    // Display Order

    private Integer displayOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
   
     @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private LaundryService service;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Final Price Calculation

    // public BigDecimal calculateTotalPrice() {

    //     BigDecimal gstAmount =
    //             basePrice.multiply(
    //                     BigDecimal.valueOf(gst / 100));

    //     return basePrice.add(gstAmount);
    // }

}