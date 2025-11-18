package com.github.lunasis.domain.post.repository;

import com.github.lunasis.domain.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") UUID id);
}
