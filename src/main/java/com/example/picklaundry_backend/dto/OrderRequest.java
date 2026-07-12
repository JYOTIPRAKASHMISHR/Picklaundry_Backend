package com.example.picklaundry_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    // Base / Express / Premium
    private String selectedPlan;

    private String pickupAddress;

    private String paymentMethod;

    private String specialInstruction;

    private LocalDateTime pickupDate;

    private List<OrderItemRequest> items;

}