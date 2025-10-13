package com.github.lunasis.domain.chat.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatsExceptions implements ApiExceptions {

    USER_NOT_ALLOWED("자신의 채팅 방에만 요청을 보낼 수 있습니다", 403);

    private final String message;
    private final Integer code;
}
