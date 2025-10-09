package com.github.lunasis.domain.auth.controller;

import com.github.lunasis.domain.auth.dto.request.ExchangeTokenRequest;
import com.github.lunasis.domain.auth.dto.response.LoginResponse;
import com.github.lunasis.domain.auth.service.AuthService;
import com.github.lunasis.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/exchange")
    public ApiResponse<LoginResponse> exchangeToken(@RequestBody ExchangeTokenRequest exchangeTokenRequest) {

        return ApiResponse.ok(authService.exchangeToken(exchangeTokenRequest));
    }
}
