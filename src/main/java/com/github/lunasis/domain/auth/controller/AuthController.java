package com.github.lunasis.domain.auth.controller;

import com.github.lunasis.domain.auth.dto.request.AppleLoginRequest;
import com.github.lunasis.domain.auth.dto.request.GoogleLoginRequest;
import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.request.RefreshTokenRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.dto.response.TokenResponse;
import com.github.lunasis.domain.auth.service.AuthService;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    @Operation(summary = "구글 로그인")
    public ApiResponse<LoginResponse> googleLogin(@RequestBody @Valid GoogleLoginRequest googleLoginRequest) {
        log.info("[Controller] 구글 로그인 요청 수신 - loginCode: {}", googleLoginRequest.loginCode());
        LoginResponse response = authService.googleLogin(googleLoginRequest.loginCode());
        log.info("[Controller] 구글 로그인 응답 반환 - nickname: {}", response.nickname());
        return ApiResponse.ok(response);
    }

    @PostMapping("/apple")
    @Operation(summary = "애플 로그인")
    public ApiResponse<LoginResponse> appleLogin(@RequestBody @Valid AppleLoginRequest appleLoginRequest) {
        return ApiResponse.ok(authService.appleLogin(appleLoginRequest));
    }


    @DeleteMapping
    @Operation(summary = "로그아웃")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) {

        authService.logout(logoutRequest);
        return ApiResponse.ok();
    }

    @PatchMapping
    @Operation(summary = "토큰 재발급")
    public ApiResponse<TokenResponse> regenerateToken(@RequestHeader("Authorization") String header,
                                                      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        String accessToken = header.replace("Bearer ", "");
        return ApiResponse.ok(authService.regenerateToken(accessToken, refreshTokenRequest));
    }

}
