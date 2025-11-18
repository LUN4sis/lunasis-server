package com.github.lunasis.domain.post.dto.request;

import com.github.lunasis.domain.post.entity.Category;

public record CreatePostRequest(
        Category category,
        String title,
        String content
) {
}
