package com.github.lunasis.domain.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LlmGuestChatRequest(
        UUID chatRoomId,
        String question
) {
    public static LlmGuestChatRequest from(GuestQuestionRequest request) {
        return new LlmGuestChatRequest(request.anonymousId(), request.question());
    }
}
