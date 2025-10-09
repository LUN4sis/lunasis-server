package com.github.lunasis.domain.auth.controller;

import com.github.lunasis.domain.auth.dto.request.ExchangeTokenRequest;
import com.github.lunasis.domain.auth.dto.request.LogoutRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.service.AuthService;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/exchange")
    @Operation(summary = "토큰 교환 api")
    public ApiResponse<LoginResponse> exchangeToken(@RequestBody @Valid ExchangeTokenRequest exchangeTokenRequest) {

        return ApiResponse.ok(authService.exchangeToken(exchangeTokenRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) {

        authService.logout(logoutRequest);
        return ApiResponse.ok();
    }
}
