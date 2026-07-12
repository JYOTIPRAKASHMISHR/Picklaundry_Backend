package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.dto.LoginRequest;
import com.example.picklaundry_backend.dto.LoginResponse;
import com.example.picklaundry_backend.dto.RegisterCustomerRequest;
import com.example.picklaundry_backend.dto.RegisterPartnerRequest;
import com.example.picklaundry_backend.dto.UserResponse;
import com.example.picklaundry_backend.entity.DeliveryPartner;
import com.example.picklaundry_backend.entity.RefreshToken;
import com.example.picklaundry_backend.entity.User;
import com.example.picklaundry_backend.enums.Role;
import com.example.picklaundry_backend.repository.DeliveryPartnerRepository;
import com.example.picklaundry_backend.repository.UserRepository;
import com.example.picklaundry_backend.security.JwtService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DeliveryPartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public String registerCustomer(
            RegisterCustomerRequest request) {

        if (userRepository.existsByMobile(
                request.getMobile())) {
            throw new RuntimeException(
                    "Mobile already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        return "Customer Registered";
    }

    public String registerPartner(RegisterPartnerRequest request) {

    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
        throw new RuntimeException("Email is required");
    }

    if (request.getMobile() == null || request.getMobile().trim().isEmpty()) {
        throw new RuntimeException("Mobile is required");
    }

    if (userRepository.existsByMobile(request.getMobile())) {
        throw new RuntimeException("Mobile already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    User user = User.builder()
            .fullName(request.getFullName())
            .email(request.getEmail().trim())
            .mobile(request.getMobile().trim())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.DELIVERY_PARTNER)
            .build();

    userRepository.save(user);

    DeliveryPartner partner = DeliveryPartner.builder()
            .user(user)
            .build();

    partnerRepository.save(partner);

    return "Partner Registered";
}

    public LoginResponse login(LoginRequest request) {

        // Manual Super Admin Login
    if (request.getMobile().equals("1234567890")
            && request.getPassword().equals("admin123")) {

       String accessToken = jwtService.generateSuperAdminToken();

return new LoginResponse(
        accessToken,
        "",
        "SUPER_ADMIN",
        0L,
        "Super Admin"
);
    }

    User user = userRepository
            .findByMobile(request.getMobile())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        throw new RuntimeException(
                "Invalid Credentials");
    }

    String accessToken =
        jwtService.generateToken(user.getId());

RefreshToken refreshToken =
        refreshTokenService.createRefreshToken(user);

return new LoginResponse(

        accessToken,

        refreshToken.getToken(),

        user.getRole().name(),

        user.getId(),

        user.getFullName()

);
        }


        public List<UserResponse> getAllUsers(){

    List<User> users =
            userRepository.findAll();


    return users.stream()
            .map(user ->
                    UserResponse.builder()

                    .id(user.getId())

                    .fullName(
                        user.getFullName()
                    )

                    .email(
                        user.getEmail()
                    )

                    .mobile(
                        user.getMobile()
                    )

                    .role(
                        user.getRole().name()
                    )

                    .active(
                        user.getActive()
                    )

                    .createdAt(
                        user.getCreatedAt()
                    )

                    .build()

            )
            .toList();
}

}
