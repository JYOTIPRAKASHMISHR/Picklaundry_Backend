package com.example.picklaundry_backend.dto;



import lombok.Data;

@Data
public class RegisterCustomerRequest {

    private String fullName;
    private String email;
    private String mobile;
    private String password;
}
