package com.github.lunasis.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record LlmChatResponse(
        @NotNull String answer,
        @Nullable String title,
        @JsonProperty("safety_info") @Nullable Map<String, Object> safetyInfo
) {
}
