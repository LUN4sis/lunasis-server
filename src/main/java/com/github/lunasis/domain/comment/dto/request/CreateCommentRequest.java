package com.github.lunasis.domain.comment.dto.request;

import java.util.UUID;

public record CreateCommentRequest(
        String content,
        UUID parentCommentId
) {
}
