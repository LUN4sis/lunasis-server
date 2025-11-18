package com.github.lunasis.domain.post.service;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.post.repository.PostRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostInfoResponse createPost(UUID userId, CreatePostRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        //새 포스트 생성
        Post newPost = Post.builder()
                .category(request.category())
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();

        postRepository.save(newPost);

        return PostInfoResponse.of(user, newPost);
    }
}
