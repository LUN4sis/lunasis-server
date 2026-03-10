package com.github.lunasis.domain.user.dto.request;

import com.github.lunasis.domain.user.entity.ChatSetting.Level;
import jakarta.validation.constraints.NotNull;

public record UpdateChatSetting(
        @NotNull String chatNickName,
        @NotNull Level warmth,
        @NotNull Level enthusiastic,
        @NotNull Level formal,
        @NotNull String personalSetting
) {
}
