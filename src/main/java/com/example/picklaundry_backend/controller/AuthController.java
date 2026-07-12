package com.example.picklaundry_backend.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
// import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.picklaundry_backend.dto.LoginRequest;
import com.example.picklaundry_backend.dto.LoginResponse;
import com.example.picklaundry_backend.dto.RefreshTokenRequest;
import com.example.picklaundry_backend.dto.RefreshTokenResponse;
import com.example.picklaundry_backend.dto.RegisterCustomerRequest;
import com.example.picklaundry_backend.dto.RegisterPartnerRequest;
import com.example.picklaundry_backend.dto.UserResponse;
import com.example.picklaundry_backend.entity.RefreshToken;
import com.example.picklaundry_backend.repository.UserRepository;
import com.example.picklaundry_backend.security.JwtService;
import com.example.picklaundry_backend.service.AuthService;
import com.example.picklaundry_backend.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register/customer")
    public String registerCustomer(
            @RequestBody
            RegisterCustomerRequest request) {

        return authService
                .registerCustomer(request);
    }

    @PostMapping("/register/partner")
    public String registerPartner(
            @RequestBody
            RegisterPartnerRequest request) {

        return authService
                .registerPartner(request);
    }

   @PostMapping("/login")
public LoginResponse login(
        @RequestBody LoginRequest request) {

    return authService.login(request);
}

@PostMapping("/refresh")
public RefreshTokenResponse refreshToken(
        @RequestBody RefreshTokenRequest request) {

    RefreshToken refreshToken =
            refreshTokenService.verifyRefreshToken(
                    request.getRefreshToken());

    String accessToken =
            jwtService.generateToken(
                    refreshToken.getUser().getId());

    return new RefreshTokenResponse(accessToken);

}
@PostMapping("/logout")
public String logout(
        @RequestBody RefreshTokenRequest request) {

    refreshTokenService.deleteRefreshToken(
            request.getRefreshToken());

    return "Logged Out Successfully";

}

@GetMapping("/user-count")
public ResponseEntity<Long> getUserCount() {

    return ResponseEntity.ok(
            userRepository.count()
    );

}

@GetMapping("/users")
public ResponseEntity<List<UserResponse>> getAllUsers(){

    return ResponseEntity.ok(
        authService.getAllUsers()
    );

}

}
