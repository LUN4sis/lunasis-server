package com.github.lunasis.domain.chat.dto.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LlmChatResponse(
        @NotNull String answer,
        @Nullable String title,
        @NotNull @Size(min = 1) float[] embedding
) {
}
