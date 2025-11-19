package com.github.lunasis.domain.comment.exception;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentExceptions implements ApiExceptions {

    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", 404),
    UNAUTHORIZED_COMMENT_MODIFICATION("댓글 수정 권한이 없습니다.", 403);

    private final String message;
    private final Integer code;
}
