package com.github.lunasis.domain.chat.dto.response;

import lombok.Builder;

@Builder
public record ChatResponse(
        String answer
) {
}
