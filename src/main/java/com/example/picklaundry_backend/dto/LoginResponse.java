package com.example.picklaundry_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    // JWT Access Token
    private String accessToken;

    // Refresh Token
    private String refreshToken;

    private String role;

    private Long userId;

    private String fullName;

}