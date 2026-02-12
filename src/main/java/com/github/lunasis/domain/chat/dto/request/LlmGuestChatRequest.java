package com.github.lunasis.domain.chat.dto.request;

import java.util.UUID;

public record LlmGuestChatRequest(
        UUID chatRoomId,
        String question
) {
    public static LlmGuestChatRequest from(GuestQuestionRequest request) {
        return new LlmGuestChatRequest(request.anonymousId(), request.question());
    }
}
