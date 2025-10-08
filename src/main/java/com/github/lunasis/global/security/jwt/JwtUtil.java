package com.github.lunasis.global.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.lunasis.domain.auth.entity.ExchangeToken;
import com.github.lunasis.domain.auth.entity.RefreshToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.ExchangeTokenRepository;
import com.github.lunasis.domain.auth.repository.RefreshTokenRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.property.JwtProperty;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ExchangeTokenRepository exchangeTokenRepository;


    @Bean
    public Algorithm algorithm() {

        return Algorithm.HMAC256(jwtProperty.getKey());
    }

    public String extractToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {

            return authorization.substring(7);

        } else return null;
    }

    public boolean validateToken(String token) {

        if (Objects.isNull(token)) {
            return false;
        }

        try {

            JWT.require(algorithm()).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {

            throw AuthExceptions.ACCESS_TOKEN_EXPIRED.toException();
        } catch (Exception e) {

            return false;
        }

    }

    public UUID extractUUID(String token) {

        return UUID.fromString(
                JWT.require(algorithm())
                        .build()
                        .verify(token)
                        .getClaim("id")
                        .asString()
        );
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtProperty.getTokenExpiration(), ChronoUnit.HOURS))
                .withClaim("id", user.getId().toString())
                .withClaim("type", TokenType.ACCESS.name())
                .sign(algorithm());
    }

    public String generateRefreshToken(User user) {

        String token = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtProperty.getRefreshExpiration(), ChronoUnit.HOURS))
                .withClaim("id", user.getId().toString())
                .withClaim("type", TokenType.REFRESH.name())
                .sign(algorithm());

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .refreshToken(token)
                .ttl(jwtProperty.getRefreshExpiration())
                .build();

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public String generateExchangeToken(String accessToken, String refreshToken, boolean firstLogin) {

        ExchangeToken exchangeToken = exchangeTokenRepository.save(ExchangeToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstLogin(firstLogin)
                .ttl(5)
                .build());

        return exchangeToken.getId();
    }


    public enum TokenType {
        ACCESS,
        REFRESH
    }
}

