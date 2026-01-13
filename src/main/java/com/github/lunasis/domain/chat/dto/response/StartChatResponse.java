package com.github.lunasis.domain.chat.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record StartChatResponse(

        UUID chatRoomId,
        String title,
        String answer
) {
}
