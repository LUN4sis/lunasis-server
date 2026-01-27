package com.github.lunasis.domain.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SaveMemory(
        @NotNull UUID userId,
        @NotNull UUID chatRoomId,
        @Nullable String savedMemory,
        @Nullable String sessionMemory,
        @NotNull @Size(min = 1) float[] embedding
) {
}
