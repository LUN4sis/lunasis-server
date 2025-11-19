package com.github.lunasis.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record ModifyCommentResponse(

        String content
) {
}
