package com.github.lunasis.domain.auth.service;

import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.request.RefreshTokenRequest;
import com.github.lunasis.domain.auth.dto.response.GoogleUserInfo;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.dto.response.TokenResponse;
import com.github.lunasis.domain.auth.entity.RefreshToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.RefreshTokenRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.property.GoogleProperty;
import com.github.lunasis.global.security.jwt.JwtUtil;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;
    private final GoogleProperty googleProperty;

    public LoginResponse googleLogin(String loginCode) {
        String googleAccessToken = getAccessToken(loginCode);
        GoogleUserInfo googleUserInfo = getUserInfo(googleAccessToken);

        Optional<User> optionalUser = userRepository.findByOauthId(googleUserInfo.id());

        User user = optionalUser.orElseGet(() -> from(googleUserInfo));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstLogin(user.getFirstLogin())
                .nickname(user.getNickname())
                .privateChat(user.getPrivateChat())
                .build();
    }

    private User from(GoogleUserInfo googleUserInfo) {
        return userRepository.save(
                User.builder()
                        .nickname(googleUserInfo.name())
                        .oauthId(googleUserInfo.id())
                        .profile(googleUserInfo.picture())
                        .build()
        );
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
    }

    private String getAccessToken(String loginCode) {
        Map<String, Object> response = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("code", loginCode)
                        .with("client_id", googleProperty.getClientId())
                        .with("client_secret", googleProperty.getClientSecret())
                        .with("redirect_uri", googleProperty.getRedirectUri()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        return (String) Objects.requireNonNull(response).get("access_token");

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

        RefreshToken redisRefreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.refreshToken())
                .orElseThrow(AuthExceptions.REFRESH_TOKEN_EXPIRED::toException);

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
