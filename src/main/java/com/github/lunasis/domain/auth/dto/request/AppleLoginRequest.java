package com.github.lunasis.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AppleLoginRequest(
        @NotBlank String loginCode,
        @NotBlank String name
) {
}
