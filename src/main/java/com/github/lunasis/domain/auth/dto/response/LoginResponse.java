package com.github.lunasis.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        boolean firstLogin,
        String nickname,
        boolean privateChat
) {
}
