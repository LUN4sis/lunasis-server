package com.github.lunasis.domain.chat.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StartChatResponse(

        UUID chatRoomId,
        String answer
) {
}
