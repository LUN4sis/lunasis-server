package com.github.lunasis.domain.post.controller;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.request.ModifyPostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.dto.response.PostListResponse;
import com.github.lunasis.domain.post.entity.Category;
import com.github.lunasis.domain.post.service.PostService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "게시물 목록 조회")
    public ApiResponse<Page<PostListResponse>> getPosts(@AuthenticationPrincipal User user,
                                                        @RequestParam(required = false) Category category,
                                                        @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ApiResponse.ok(postService.getPosts(user.getId(), category, pageable));
    }

    @PostMapping
    @Operation(summary = " 새 게시물 생성 ")
    public ApiResponse<PostInfoResponse> createPost(@AuthenticationPrincipal User user,
                                                    @RequestBody CreatePostRequest request) {

        return ApiResponse.ok(postService.createPost(user.getId(), request));
    }

    @GetMapping("/{postId}")
    @Operation(summary = " 게시물 상세 조회 ")
    public ApiResponse<PostInfoResponse> getPost(@AuthenticationPrincipal User user, @PathVariable UUID postId) {

        return ApiResponse.ok(postService.getPost(user.getId(), postId));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = " 게시물 수정 ")
    public ApiResponse<PostInfoResponse> updatePost(@AuthenticationPrincipal User user, @PathVariable UUID postId,
                                                    @RequestBody ModifyPostRequest request) {

        return ApiResponse.ok(postService.updatePost(user, postId, request));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = " 게시물 삭제 ")
    public ApiResponse<Void> deletePost(@AuthenticationPrincipal User user, @PathVariable UUID postId) {

        postService.deletePost(user, postId);
        return ApiResponse.ok();
    }

    @PostMapping("/{postId}/bookmark")
    @Operation(summary = "게시물 북마크 추가")
    public ApiResponse<Void> addBookmark(@AuthenticationPrincipal User user, @PathVariable UUID postId) {

        postService.addBookmark(user.getId(), postId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{postId}/bookmark")
    @Operation(summary = "게시물 북마크 삭제")
    public ApiResponse<Void> removeBookmark(@AuthenticationPrincipal User user, @PathVariable UUID postId) {

        postService.removeBookmark(user.getId(), postId);
        return ApiResponse.ok();
    }
}
