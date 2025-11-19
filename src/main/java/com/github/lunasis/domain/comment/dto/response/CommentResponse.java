package com.github.lunasis.domain.comment.dto.response;

import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CommentResponse(
        UUID commentId,
        UUID parentCommentId,
        SimpleAuthorResponse author,
        String content,
        boolean isAuthor,
        boolean isEdited,
        List<CommentResponse> replies,
        LocalDateTime createdAt
) {
}
