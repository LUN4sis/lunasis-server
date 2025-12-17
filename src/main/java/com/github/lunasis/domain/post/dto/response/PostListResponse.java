package com.github.lunasis.domain.post.dto.response;

import com.github.lunasis.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PostListResponse(
        UUID postId,
        SimpleAuthorResponse author,
        String title,
        String content,
        int commentCount,
        boolean isBookmarked,
        LocalDateTime createdAt
) {
    public static PostListResponse from(Post post, boolean isBookmarked) {
        return PostListResponse.builder()
                .postId(post.getId())
                .author(SimpleAuthorResponse.from(post.getUser()))
                .title(post.getTitle())
                .content(post.getContent())
                .commentCount(post.getCommentCount())
                .isBookmarked(isBookmarked)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
