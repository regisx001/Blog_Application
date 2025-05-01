package com.regisx001.blog.services;

import java.util.UUID;

import com.regisx001.blog.domain.entities.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken createRefreshToken(UUID userId);

    public RefreshToken verifyExpiration(RefreshToken token);

    public void deleteByUserId(UUID userId);
}
