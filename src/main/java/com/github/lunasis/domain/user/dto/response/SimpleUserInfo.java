package com.github.lunasis.domain.user.dto.response;

import lombok.Builder;

@Builder
public record SimpleUserInfo(
        String nickname,
        boolean privateChat
) {
}
