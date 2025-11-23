package com.github.lunasis.domain.post.service;

import com.github.lunasis.domain.post.dto.request.CreatePostRequest;
import com.github.lunasis.domain.post.dto.request.ModifyPostRequest;
import com.github.lunasis.domain.post.dto.response.PostInfoResponse;
import com.github.lunasis.domain.post.dto.response.PostListResponse;
import com.github.lunasis.domain.post.dto.response.SimpleAuthorResponse;
import com.github.lunasis.domain.post.entity.Category;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.post.exception.PostExceptions;
import com.github.lunasis.domain.post.repository.PostRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public PostInfoResponse getPost(UUID userId, UUID postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        boolean isAuthor = post.getUser().getId().equals(userId);
        boolean isBookmarked = user.isBookmarked(postId);

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

    @Transactional
    public PostInfoResponse updatePost(User user, UUID postId, ModifyPostRequest request) {

        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        //작성자 확인
        if (!post.getUser().getId().equals(user.getId())) {
            throw PostExceptions.UNAUTHORIZED_POST_MODIFICATION.toException();
        }

        //포스트 수정
        post.updatePost(request.title(), request.content());

        return PostInfoResponse.of(user, post);
    }

    @Transactional
    public void deletePost(User user, UUID postId) {

        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostExceptions.POST_NOT_FOUND::toException);

        //작성자 확인
        if (!post.getUser().getId().equals(user.getId())) {
            throw PostExceptions.UNAUTHORIZED_POST_MODIFICATION.toException();
        }

        //포스트 삭제
        postRepository.delete(post);
    }

    @Transactional
    public void addBookmark(UUID userId, UUID postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        if (!postRepository.existsById(postId)) {
            throw PostExceptions.POST_NOT_FOUND.toException();
        }

        user.addBookmark(postId);
    }

    @Transactional
    public void removeBookmark(UUID userId, UUID postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        if (!user.isBookmarked(postId)) {
            throw PostExceptions.BOOKMARK_NOT_FOUND.toException();
        }

        user.removeBookmark(postId);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPosts(UUID userId, Category category, Pageable pageable) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        Page<Post> posts = postRepository.findAllWithUserAndComments(category, pageable);

        return posts.map(post -> PostListResponse.from(post, user.isBookmarked(post.getId())));
    }
}
