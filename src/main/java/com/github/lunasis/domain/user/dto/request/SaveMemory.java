package com.github.lunasis.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SaveMemory(
        @NotNull UUID userId,
        @NotNull String savedMemory,
        @NotNull @Size(min = 1) float[] embedding
) {
}
