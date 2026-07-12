package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.entity.RefreshToken;
import com.example.picklaundry_backend.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void deleteRefreshToken(String token);

    void deleteUserTokens(User user);

}