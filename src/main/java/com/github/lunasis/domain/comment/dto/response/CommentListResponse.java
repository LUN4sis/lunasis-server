package com.github.lunasis.domain.comment.dto.response;

import com.github.lunasis.domain.comment.entity.Comment;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CommentListResponse(
        UUID commentId,
        SimpleAuthorResponse author,
        String content,
        boolean isAuthor,
        boolean isEdited,
        int replyCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyResponse> replies
) {
    public static CommentListResponse from(Comment comment, UUID currentUserId) {
        List<ReplyResponse> replyResponses = comment.getReplies().stream()
                .map(reply -> ReplyResponse.from(reply, currentUserId))
                .toList();

        return CommentListResponse.builder()
                .commentId(comment.getId())
                .author(SimpleAuthorResponse.from(comment.getUser()))
                .content(comment.getContent())
                .isAuthor(comment.getUser().getId().equals(currentUserId))
                .isEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt()))
                .replyCount(comment.getReplies().size())
                .createdAt(comment.getCreatedAt())
                .replies(replyResponses)
                .build();
    }
}