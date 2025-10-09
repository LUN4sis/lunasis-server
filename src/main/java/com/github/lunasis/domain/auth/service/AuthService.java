package com.github.lunasis.domain.auth.service;

import com.github.lunasis.domain.auth.dto.request.ExchangeTokenRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.entity.ExchangeToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.ExchangeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ExchangeTokenRepository exchangeTokenRepository;

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
}
