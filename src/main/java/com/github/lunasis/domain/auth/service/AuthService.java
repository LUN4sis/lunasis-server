package com.github.lunasis.domain.auth.service;

import com.github.lunasis.domain.auth.dto.request.ExchangeTokenRequest;
import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.request.RefreshTokenRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.dto.response.TokenResponse;
import com.github.lunasis.domain.auth.entity.ExchangeToken;
import com.github.lunasis.domain.auth.entity.RefreshToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.ExchangeTokenRepository;
import com.github.lunasis.domain.auth.repository.RefreshTokenRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ExchangeTokenRepository exchangeTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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

    public TokenResponse regenerateToken(String accessToken, RefreshTokenRequest refreshTokenRequest) {

        if (Objects.isNull(accessToken)) {
            throw AuthExceptions.Token_Not_Exist.toException();
        }

        RefreshToken redisRefreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.refreshToken()).orElseThrow(AuthExceptions.REFRESH_TOKEN_EXPIRED::toException);

        UUID userId = jwtUtil.extractUUID(accessToken);

        if (!userId.equals(redisRefreshToken.getUserId())) {
            throw AuthExceptions.TOKEN_NOT_PAIR.toException();
        }

        refreshTokenRepository.delete(redisRefreshToken);
        User user = userRepository.findById(userId).orElseThrow(AuthExceptions.INVALID_TOKEN::toException);

        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
