package com.github.lunasis.domain.comment.dto.response;

import com.github.lunasis.domain.comment.entity.Comment;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReplyResponse(
        UUID commentId,
        SimpleAuthorResponse author,
        String content,
        boolean isAuthor,
        boolean isEdited,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReplyResponse from(Comment comment, UUID currentUserId) {
        return ReplyResponse.builder()
                .commentId(comment.getId())
                .author(SimpleAuthorResponse.from(comment.getUser()))
                .content(comment.getContent())
                .isAuthor(comment.getUser().getId().equals(currentUserId))
                .isEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
