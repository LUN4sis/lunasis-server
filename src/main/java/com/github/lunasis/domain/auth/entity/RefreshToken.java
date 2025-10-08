package com.github.lunasis.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    @Builder.Default
    String id = UUID.randomUUID().toString();

    UUID userId;

    @Indexed
    String refreshToken;

    @TimeToLive(unit = TimeUnit.HOURS)
    long ttl;
}
