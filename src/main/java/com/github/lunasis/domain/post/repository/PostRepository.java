package com.github.lunasis.domain.post.repository;

import com.github.lunasis.domain.post.entity.Post;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
