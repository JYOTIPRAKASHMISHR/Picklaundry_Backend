package com.example.picklaundry_backend.dto;

import lombok.Data;

@Data
public class AddServiceRequest {

    private String serviceName;

    private String description;

    private String icon;

    private boolean active;
}