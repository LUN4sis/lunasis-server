package com.github.lunasis.domain.chat.dto.request;

import com.github.lunasis.domain.product.entity.ProductCategory;
import java.util.List;
import java.util.UUID;

public record LlmChatRequest(
        UUID userId,
        UUID chatRoomId,
        UUID question,
        PreferenceRequest preference
) {
    public record PreferenceRequest(
            Level warmth,
            Level enthusiastic,
            Level formal,
            List<ProductCategory> productCategories,
            String personalSetting,
            String userName,
            Integer age
    ) {
    }

    public enum Level {
        HIGH, DEFAULT, LESS
    }
}
