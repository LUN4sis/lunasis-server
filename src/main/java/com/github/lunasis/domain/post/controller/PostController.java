package com.github.lunasis.domain.post.controller;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.request.ModifyPostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.service.PostService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(description = " 새 게시물 생성 ")
    public ApiResponse<PostInfoResponse> createPost(@AuthenticationPrincipal User user,
                                                    @RequestBody CreatePostRequest request) {

        return ApiResponse.ok(postService.createPost(user.getId(), request));
    }

    @GetMapping("/{postId}")
    @Operation(description = " 게시물 상세 조회 ")
    public ApiResponse<PostInfoResponse> getPost(@AuthenticationPrincipal User user, @PathVariable UUID postId) {

        return ApiResponse.ok(postService.getPost(user, postId));
    }

    @PatchMapping("/{postId}")
    @Operation(description = " 게시물 수정 ")
    public ApiResponse<PostInfoResponse> updatePost(@AuthenticationPrincipal User user, @PathVariable UUID postId,
                                                    @RequestBody ModifyPostRequest request) {

        return ApiResponse.ok(postService.updatePost(user, postId, request));

    }
}
