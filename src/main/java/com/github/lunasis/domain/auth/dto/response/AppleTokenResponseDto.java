package com.github.lunasis.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleTokenResponseDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        String expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("id_token")
        String idToken
) {
}
