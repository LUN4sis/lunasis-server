package com.github.lunasis.domain.auth.service;

import com.github.lunasis.domain.auth.dto.request.ExchangeTokenRequest;
import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.entity.ExchangeToken;
import com.github.lunasis.domain.auth.entity.RefreshToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.ExchangeTokenRepository;
import com.github.lunasis.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ExchangeTokenRepository exchangeTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest) {

        ExchangeToken exchangeToken = exchangeTokenRepository.findById(exchangeTokenRequest.exchangeToken())
                .orElseThrow(AuthExceptions.ACCESS_TOKEN_EXPIRED::toException);

        exchangeTokenRepository.delete(exchangeToken);

        return LoginResponse.builder()
                .accessToken(exchangeToken.getAccessToken())
                .refreshToken(exchangeToken.getRefreshToken())
                .firstLogin(exchangeToken.isFirstLogin())
                .nickname(exchangeToken.getName())
                .privateChat(exchangeToken.isPrivateChat())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public void logout(LogoutRequest logoutRequest) {

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(logoutRequest.refreshToken())
                .orElseThrow(AuthExceptions.ACCESS_TOKEN_EXPIRED::toException);

        refreshTokenRepository.delete(refreshToken);
    }
}
