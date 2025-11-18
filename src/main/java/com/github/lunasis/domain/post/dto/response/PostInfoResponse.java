package com.github.lunasis.domain.post.dto.response;

import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PostInfoResponse(
        UUID postId,
        SimpleAuthorResponse author,
        String title,
        String content,
        boolean isAuthor,
        boolean isBookmarked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    //새 포스터 생성이여서 북마크 여부는 false, 작성자 여부는 true로 고정
    public static PostInfoResponse of(User user, Post post) {
        return PostInfoResponse.builder()
                .postId(post.getId())
                .author(SimpleAuthorResponse.from(user))
                .title(post.getTitle())
                .content(post.getContent())
                .isAuthor(true)
                .isBookmarked(false)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
