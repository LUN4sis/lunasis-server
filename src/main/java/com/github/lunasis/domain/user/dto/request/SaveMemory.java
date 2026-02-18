package com.github.lunasis.domain.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SaveMemory(
        @NotNull UUID userId,
        @NotNull UUID chatRoomId,
        @Nullable String savedMemory,
        @Nullable String sessionSummary,
        @Nullable float[] embedding
) {
}
