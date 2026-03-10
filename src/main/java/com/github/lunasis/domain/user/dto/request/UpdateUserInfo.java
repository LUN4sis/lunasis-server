package com.github.lunasis.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserInfo(

        @Schema(description = "사용자 닉네임")
        String nickname,
        @Schema(description = "사용자 나이")
        Integer age

) {
}
