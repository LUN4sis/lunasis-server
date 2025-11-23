package com.github.lunasis.domain.comment.repository;

import com.github.lunasis.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Comment> findByIdWithUser(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.replies r " +
            "LEFT JOIN FETCH r.user " +
            "WHERE c.post.id = :postId AND c.parent IS NULL " +
            "ORDER BY c.createdAt DESC")
    List<Comment> findByPostIdWithReplies(@Param("postId") UUID postId);
}
