package com.github.lunasis.domain.user.repository;

import com.github.lunasis.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByOauthId(String oauthId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
