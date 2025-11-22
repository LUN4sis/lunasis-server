package com.github.lunasis.domain.comment.controller;

import com.github.lunasis.domain.comment.dto.request.CreateCommentRequest;
import com.github.lunasis.domain.comment.dto.request.ModifyCommentRequest;
import com.github.lunasis.domain.comment.dto.response.CommentResponse;
import com.github.lunasis.domain.comment.dto.response.ModifyCommentResponse;
import com.github.lunasis.domain.comment.service.CommentService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@PreAuthorize("isAuthenticated()")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResponse> createComment(@AuthenticationPrincipal User user,
                                                      @PathVariable UUID postId,
                                                      @RequestBody CreateCommentRequest request) {

        return ApiResponse.ok(commentService.createComment(user.getId(), postId, request));
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public ApiResponse<ModifyCommentResponse> updateComment(@AuthenticationPrincipal User user,
                                                            @PathVariable UUID commentId,
                                                            @RequestBody ModifyCommentRequest request) {

        return ApiResponse.ok(commentService.updateComment(user, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제, Soft Delete")
    public ApiResponse<Void> deleteComment(@AuthenticationPrincipal User user,
                                           @PathVariable UUID commentId) {

        commentService.deleteComment(user, commentId);
        return ApiResponse.ok();
    }
}
