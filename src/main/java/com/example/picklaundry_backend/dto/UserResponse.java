package com.example.picklaundry_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String mobile;

    private String role;

    private Boolean active;

    private LocalDateTime createdAt;
}