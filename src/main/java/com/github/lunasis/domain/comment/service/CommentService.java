package com.github.lunasis.domain.comment.service;

import com.github.lunasis.domain.comment.dto.request.CreateCommentRequest;
import com.github.lunasis.domain.comment.dto.response.CommentResponse;
import com.github.lunasis.domain.comment.entity.Comment;
import com.github.lunasis.domain.comment.repository.CommentRepository;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.post.exception.PostExceptions;
import com.github.lunasis.domain.post.repository.PostRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse createComment(UUID userId, UUID postId, CreateCommentRequest request) {

        User user = userRepository.findById(userId).orElseThrow(UserExceptions.USER_NOT_FOUND::toException);
        Post post = postRepository.findById(postId).orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        Comment comment = commentRepository.save(Comment.builder()
                .content(request.content())
                .user(user)
                .parent(null)
                .post(post)
                .build());

        post.getComments().add(comment);

        return CommentResponse.builder()
                .commentId(comment.getId())
                .parentCommentId(null)
                .author(SimpleAuthorResponse.from(user))
                .content(comment.getContent())
                .isAuthor(true)
                .isEdited(false)
                .replies(new ArrayList<>())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
