package com.github.lunasis.domain.post.repository;

import com.github.lunasis.domain.post.entity.Category;
import com.github.lunasis.domain.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") UUID id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") UUID id);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE (:category IS NULL OR p.category = :category)")
    Page<Post> findAllWithUserAndComments(@Param("category") Category category, Pageable pageable);
}
