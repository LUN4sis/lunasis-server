package com.github.lunasis.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserInfo(

        @Schema(description = "사용자 채팅 닉네임")
        String chatNickname,
        @Schema(description = "사용자 나이")
        Integer age

) {
}
