package com.github.lunasis.domain.user.dto.response;

import com.github.lunasis.domain.user.entity.SavedMemory;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SavedMemoryResponse(
        UUID savedMemoryId,
        String savedMemory
) {
    public static SavedMemoryResponse from(SavedMemory savedMemory) {
        return SavedMemoryResponse.builder()
                .savedMemoryId(savedMemory.getId())
                .savedMemory(savedMemory.getSummary())
                .build();
    }
}
