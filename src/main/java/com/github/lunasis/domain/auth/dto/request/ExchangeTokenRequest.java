package com.github.lunasis.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ExchangeTokenRequest(
        @NotBlank String exchangeToken
) {
}
