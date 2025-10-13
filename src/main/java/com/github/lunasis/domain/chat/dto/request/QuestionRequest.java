package com.github.lunasis.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record QuestionRequest(
        @NotNull String question
) {
}
