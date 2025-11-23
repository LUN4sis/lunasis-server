package com.github.lunasis.domain.post.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostExceptions implements ApiExceptions {

    POST_NOT_FOUND("포스트를 찾을 수 없습니다.", 404),
    UNAUTHORIZED_POST_MODIFICATION("포스트 수정 권한이 없습니다.", 403),
    BOOKMARK_NOT_FOUND("북마크를 찾을 수 없습니다.", 404);

    private final String message;
    private final Integer code;
}
