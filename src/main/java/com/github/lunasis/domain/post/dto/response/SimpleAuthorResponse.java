package com.github.lunasis.domain.post.dto.response;

import com.github.lunasis.domain.user.entity.User;
import lombok.Builder;

@Builder
public record SimpleAuthorResponse(
        String nickname,
        String profile
) {
    public static SimpleAuthorResponse from(User user) {
        return SimpleAuthorResponse.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .build();
    }
}
