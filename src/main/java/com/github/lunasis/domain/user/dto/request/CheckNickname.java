package com.github.lunasis.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckNickname(

        @NotBlank String nickname
) {
}
