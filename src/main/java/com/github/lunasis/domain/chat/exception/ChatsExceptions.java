package com.github.lunasis.domain.chat.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatsExceptions implements ApiExceptions {

    USER_NOT_ALLOWED("자신의 채팅 방에 관련된 요청만 보낼 수 있습니다", 403),
    CHATROOM_NOT_FOUND("해당 채팅방은 존재 하지 않습니다", 404);

    private final String message;
    private final Integer code;
}
