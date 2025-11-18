package com.github.lunasis.domain.post.controller;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.service.PostService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostInfoResponse> createPost(@AuthenticationPrincipal User user,
                                                    @RequestBody CreatePostRequest request) {

        return ApiResponse.ok(postService.createPost(user.getId(), request));
    }
}
