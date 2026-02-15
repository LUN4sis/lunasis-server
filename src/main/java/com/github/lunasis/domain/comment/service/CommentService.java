package com.github.lunasis.domain.comment.service;

import com.github.lunasis.domain.comment.dto.request.CreateCommentRequest;
import com.github.lunasis.domain.comment.dto.request.ModifyCommentRequest;
import com.github.lunasis.domain.comment.dto.response.CommentListResponse;
import com.github.lunasis.domain.comment.dto.response.CommentResponse;
import com.github.lunasis.domain.comment.dto.response.ModifyCommentResponse;
import com.github.lunasis.domain.comment.entity.Comment;
import com.github.lunasis.domain.comment.exception.CommentExceptions;
import com.github.lunasis.domain.comment.repository.CommentRepository;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.post.exception.PostExceptions;
import com.github.lunasis.domain.post.repository.PostRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public CommentResponse createComment(UUID userId, UUID postId, CreateCommentRequest request) {

        User user = userService.getUserById(userId);
        Post post = postRepository.findByIdWithComments(postId).orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        // 대댓글의 부모 댓글, 없으면 null
        Comment parent = null;

        UUID parentId = request.parentCommentId();
        // parentId가 null이 아니면 대댓글이므로 부모 댓글을 찾기
        if (parentId != null) {
            parent = post.getComments().stream()
                    .filter(comment -> comment.getId().equals(parentId))
                    .findFirst()
                    .orElseThrow(CommentExceptions.COMMENT_NOT_FOUND::toException);
        }

        Comment comment = commentRepository.save(Comment.builder()
                .content(request.content())
                .user(user)
                .parent(parent)
                .post(post)
                .build());

        post.getComments().add(comment);
        post.increaseCommentCount();

        return CommentResponse.builder()
                .commentId(comment.getId())
                .parentCommentId(parentId)
                .author(SimpleAuthorResponse.from(user))
                .content(comment.getContent())
                .isAuthor(true)
                .isEdited(false)
                .replies(null)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Transactional
    public ModifyCommentResponse updateComment(User user, UUID commentId, ModifyCommentRequest request) {

        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(CommentExceptions.COMMENT_NOT_FOUND::toException);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw CommentExceptions.UNAUTHORIZED_COMMENT_MODIFICATION.toException();
        }

        comment.updateContent(request.content());

        return ModifyCommentResponse.builder()
                .content(comment.getContent())
                .build();
    }

    @Transactional
    public void deleteComment(User user, UUID commentId) {

        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(CommentExceptions.COMMENT_NOT_FOUND::toException);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw CommentExceptions.UNAUTHORIZED_COMMENT_MODIFICATION.toException();
        }

        comment.updateContent("삭제된 댓글입니다.");
    }

    @Transactional(readOnly = true)
    public List<CommentListResponse> getCommentsByPost(UUID userId, UUID postId) {
        List<Comment> comments = commentRepository.findByPostIdWithReplies(postId);

        return comments.stream()
                .map(comment -> CommentListResponse.from(comment, userId))
                .toList();
    }
}
