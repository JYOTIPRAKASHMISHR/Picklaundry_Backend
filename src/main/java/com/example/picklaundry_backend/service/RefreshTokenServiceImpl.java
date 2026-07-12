package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.entity.RefreshToken;
import com.example.picklaundry_backend.entity.User;
import com.example.picklaundry_backend.repository.RefreshTokenRepository;
import com.example.picklaundry_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(365))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Refresh Token Not Found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh Token Revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException("Refresh Token Expired");
        }

        return refreshToken;
    }

    @Override
    public void deleteRefreshToken(String token) {

        refreshTokenRepository.deleteByToken(token);

    }

    @Override
    public void deleteUserTokens(User user) {

        refreshTokenRepository.deleteByUser(user);

    }

}