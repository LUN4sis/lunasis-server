package com.github.lunasis.domain.post.service;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.post.exception.PostExceptions;
import com.github.lunasis.domain.post.repository.PostRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
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

    @Transactional
    public PostInfoResponse getPost(User user, UUID postId) {

        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        boolean isAuthor = post.getUser().getId().equals(user.getId());

        //TODO: 북마크 여부는 추후 구현
        boolean isBookmarked = false;

        return PostInfoResponse.builder()
                .postId(post.getId())
                .author(SimpleAuthorResponse.from(post.getUser()))
                .title(post.getTitle())
                .content(post.getContent())
                .isAuthor(isAuthor)
                .isBookmarked(isBookmarked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
