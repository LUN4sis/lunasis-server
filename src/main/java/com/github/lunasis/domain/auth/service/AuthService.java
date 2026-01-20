package com.github.lunasis.domain.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.lunasis.domain.auth.dto.request.AppleLoginRequest;
import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.request.RefreshTokenRequest;
import com.github.lunasis.domain.auth.dto.response.AppleTokenResponseDto;
import com.github.lunasis.domain.auth.dto.response.GoogleUserInfo;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.dto.response.TokenResponse;
import com.github.lunasis.domain.auth.entity.RefreshToken;
import com.github.lunasis.domain.auth.exceptions.AuthExceptions;
import com.github.lunasis.domain.auth.repository.RefreshTokenRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.property.AppleProperty;
import com.github.lunasis.global.property.GoogleProperty;
import com.github.lunasis.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;
    private final GoogleProperty googleProperty;
    private final AppleProperty appleProperty;

    @Transactional
    public LoginResponse googleLogin(String loginCode) {
        log.info("[구글 로그인] 시작 - loginCode: {}", loginCode);

        String googleAccessToken = getAccessToken(loginCode);
        log.info("[구글 로그인] Google Access Token 획득 성공");

        GoogleUserInfo googleUserInfo = getUserInfo(googleAccessToken);
        log.info("[구글 로그인] 사용자 정보 조회 성공 - id: {}, name: {}", googleUserInfo.id(), googleUserInfo.name());

        Optional<User> optionalUser = userRepository.findByOauthId(googleUserInfo.id());
        log.info("[구글 로그인] DB 사용자 조회 결과: {}", optionalUser.isPresent() ? "기존 사용자" : "신규 사용자");

        User user = optionalUser.orElseGet(() -> from(googleUserInfo));

        LoginResponse response = generateLoginInfo(user);
        log.info("[구글 로그인] Login Response: nickname={}, accessToken={}", response.nickname(), response.accessToken());

        return response;
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

    public LoginResponse appleLogin(AppleLoginRequest appleLoginRequest) {

        AppleTokenResponseDto appleTokenResponseDto = getAppleToken(appleLoginRequest.loginCode());
        DecodedJWT decodedJWT = getAppleUserInfo(appleTokenResponseDto);

        String appleAuthId = decodedJWT.getClaim("sub").asString();

        Optional<User> optionalUser = userRepository.findByOauthId(appleAuthId);

        User user = optionalUser.orElseGet(() -> of(appleLoginRequest.name(), appleAuthId));

        return generateLoginInfo(user);
    }

    private User of(String name, String authId) {
        return userRepository.save(
                User.builder()
                        .nickname(name)
                        .oauthId(authId)
                        .profile(null)
                        .build()
        );
    }

    private AppleTokenResponseDto getAppleToken(String loginCode) {

        try {
            return webClient.post()
                    .uri(URI.create("https://appleid.apple.com/auth/token"))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", appleProperty.getClientId())
                            .with("client_secret", makeClientSecretToken())
                            .with("code", loginCode))
                    .retrieve()
                    .bodyToMono(AppleTokenResponseDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("애플 로그인 실패" + e.getResponseBodyAsString());
            throw e;
        }

    }

    private String makeClientSecretToken() {
        String token = Jwts.builder()
                .subject(appleProperty.getClientId())
                .issuer(appleProperty.getTeamId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 600000))
                .audience()
                .add("https://appleid.apple.com")
                .and()
                .header()
                .keyId(appleProperty.getKeyId())
                .and()
                .signWith(getPrivateKey(), Jwts.SIG.ES256)
                .compact();
        log.info("[애플 로그인] 로그인 요청 인증 토큰: " + token);
        return token;
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] privateKeyBytes = Decoders.BASE64.decode(appleProperty.getClientPrivateKey());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.info("[애플 로그인] PK 생성 실패", e);
            throw new RuntimeException("애플 로그인 실패");
        }
    }

    private DecodedJWT getAppleUserInfo(AppleTokenResponseDto appleTokenResponseDto) {

        return JWT.decode(appleTokenResponseDto.idToken());

    }

    private LoginResponse generateLoginInfo(User user) {
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
