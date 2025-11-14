package com.github.lunasis.domain.user.dto.request;

import com.github.lunasis.domain.user.entity.Insurance;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UpdateUserInfo(

        @Schema(description = "사용자 닉네임")
        String nickname,
        @Schema(description = "사용자 나이")
        Integer age,
        @Schema(description = "보험 정보")
        Set<Insurance> insurance,
        @Schema(description = "비밀 채팅 설정 여부")
        boolean privateChat

) {
}
