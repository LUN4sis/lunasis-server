package com.github.lunasis.domain.chat.dto.response;

import com.github.lunasis.domain.chat.entity.ChatRoom;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChatListResponse(
        UUID chatRoomId,
        String title
) {
    public static ChatListResponse from(ChatRoom chatRoom) {
        return ChatListResponse.builder()
                .chatRoomId(chatRoom.getId())
                .title(chatRoom.getTitle())
                .build();
    }
}
