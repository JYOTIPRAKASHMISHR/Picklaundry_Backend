package com.example.picklaundry_backend.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long itemId;

    private String serviceName;

    private String itemName;

    private String selectedPlan;

    private Integer quantity;

   
private BigDecimal price;

    // private Double gst;

    private BigDecimal subtotal;

}