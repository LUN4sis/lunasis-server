package com.github.lunasis.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank String loginCode
) {
}
