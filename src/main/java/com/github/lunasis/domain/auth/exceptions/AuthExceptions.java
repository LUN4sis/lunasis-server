package com.github.lunasis.domain.auth.exceptions;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptions implements ApiExceptions {

    ACCESS_TOKEN_EXPIRED("토큰이 만료되었습니다", 404);

    private final String message;
    private final Integer code;
}
