package com.github.lunasis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GuestQuestionRequest(
        @Schema(description = "프론트 로컬에서 관리 할 임시 UUID")
        @NotNull UUID anonymousId,
        @NotNull String question
) {
}
