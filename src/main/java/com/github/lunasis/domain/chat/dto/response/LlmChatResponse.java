package com.github.lunasis.domain.chat.dto.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record LlmChatResponse(
        @NotNull String answer,
        @Nullable String title
) {
}
