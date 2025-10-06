package com.github.lunasis.domain.user.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptions implements ApiExceptions {

    USER_NOT_FOUND("유저 정보를 찾을 수 없습니다.", 403);

    private final String message;
    private final Integer code;
}
