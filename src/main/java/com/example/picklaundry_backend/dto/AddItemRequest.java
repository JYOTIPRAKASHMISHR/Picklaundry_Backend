package com.example.picklaundry_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddItemRequest {

    private Long serviceId;

    private String itemName;

    private BigDecimal basePrice;

    private BigDecimal expressPrice;

    private BigDecimal premiumPrice;

    

    private Integer displayOrder;

    private Boolean active=true;
}