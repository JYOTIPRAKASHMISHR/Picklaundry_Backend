package com.example.picklaundry_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private String orderId;

    private String customerName;

    private String mobileNumber;

    private String pickupAddress;

    private String selectedPlan;

    private Integer totalItems;

    // Total amount before any additional charges
    private BigDecimal totalAmount;

    // Final payable amount
    private BigDecimal grandTotal;

    private String orderStatus;

    private String paymentStatus;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

    private String deliveryPartnerName;

    private String deliveryPartnerMobile;
}