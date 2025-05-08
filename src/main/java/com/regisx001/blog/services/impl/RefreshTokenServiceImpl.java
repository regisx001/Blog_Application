package com.regisx001.blog.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.entities.RefreshToken;
import com.regisx001.blog.repositories.RefreshTokenRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${security.jwt.refresh-token.expiration-ms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .filter(existingToken -> existingToken.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(userRepository.findById(userId).orElseThrow());
                    newToken.setToken(UUID.randomUUID().toString());
                    newToken.setExpiryDate(LocalDateTime.now().plusNanos(refreshTokenDurationMs * 1_000_000));
                    return newToken;
                });

        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please sign in again.");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow());
    }

}
