package com.example.picklaundry_backend.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long serviceId;

    private Long serviceItemId;

    private Integer quantity;

}