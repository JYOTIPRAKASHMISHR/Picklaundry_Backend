package com.example.picklaundry_backend.entity;

import com.example.picklaundry_backend.enums.OrderStatus;
import com.example.picklaundry_backend.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Example : ORD202606260001
    @Column(nullable = false, unique = true)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(nullable = false)
    private String pickupAddress;

    private String specialInstruction;

    @Column(nullable = false)
    private String selectedPlan;

    @Column(nullable = false)
    private Integer totalItems;

 @Column(nullable = false, precision = 10, scale = 2)
private BigDecimal totalAmount;

    // @Column(nullable = false)
    // private Double totalGST;

    @Column(nullable = false, precision = 10, scale = 2)
private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private LocalDateTime pickupDate;

    private LocalDateTime deliveryDate;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        // if (status == null) {
        //     status = OrderStatus.PENDING;
        // }

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}