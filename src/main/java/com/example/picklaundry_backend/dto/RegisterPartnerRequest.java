package com.example.picklaundry_backend.dto;

import lombok.Data;

@Data
public class RegisterPartnerRequest {

    private String fullName;
    private String email;
    private String mobile;
    private String password;

    
}
