package com.github.lunasis.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@RedisHash(value = "exchange_token")
public class ExchangeToken {

    @Id
    @Builder.Default
    String id = UUID.randomUUID().toString();

    String accessToken;

    String refreshToken;

    boolean firstLogin;

    @TimeToLive(unit = TimeUnit.MINUTES)
    long ttl;
}
