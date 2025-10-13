package com.github.lunasis.domain.chat.dto.response;

import com.github.lunasis.domain.chat.entity.Chat;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChatHistoryResponse(
        UUID chatId,
        String question,
        String answer,
        String image
) {
    public static ChatHistoryResponse from(Chat chat) {

        return ChatHistoryResponse.builder()
                .chatId(chat.getId())
                .question(chat.getQuestion())
                .answer(chat.getAnswer())
                .image(chat.getImage())
                .build();
    }
}
