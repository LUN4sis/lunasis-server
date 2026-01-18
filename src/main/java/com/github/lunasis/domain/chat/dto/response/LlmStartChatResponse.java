package com.github.lunasis.domain.chat.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LlmStartChatResponse(
        @NotNull String answer,
        @NotNull String title,
        @NotNull @Size(min = 1) float[] embedding
) {
}
