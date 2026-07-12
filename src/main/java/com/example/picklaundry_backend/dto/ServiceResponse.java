package com.example.picklaundry_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceResponse {

    private Long serviceId;

    private String serviceName;

    private String description;

    private String icon;

    private boolean active;

    private List<ItemResponse> items;

    @Data
    @Builder
    public static class ItemResponse {

        private Long itemId;

        private String itemName;

        private String basePrice;

        private String expressPrice;

        private String premiumPrice;

        private Double gst;

        private Integer displayOrder;

        private Boolean active;
    }
}