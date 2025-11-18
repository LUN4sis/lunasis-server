package com.github.lunasis.domain.post.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostExceptions implements ApiExceptions {

    POST_NOT_FOUND("포스트를 찾을 수 없습니다.", 404);

    private final String message;
    private final Integer code;
}
