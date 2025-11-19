package com.github.lunasis.domain.comment.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentExceptions implements ApiExceptions {

    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", 404);

    private final String message;
    private final Integer code;
}
