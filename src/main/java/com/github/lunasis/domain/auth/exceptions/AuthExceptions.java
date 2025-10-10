package com.github.lunasis.domain.auth.exceptions;

import com.github.lunasis.global.exception.ApiExceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptions implements ApiExceptions {

    ACCESS_TOKEN_EXPIRED("토큰이 만료되었습니다", 404),
    Token_Not_Exist("토큰 정보가 헫에 존재 하지 않습니다", 404),
    TOKEN_NOT_PAIR("엑세스 토큰과 리프레쉬 토큰의 정보가 다릅니다.", 403),
    REFRESH_TOKEN_EXPIRED("리프레쉬 토큰이 만료되었습니다.", 403),
    INVALID_TOKEN("잘못된 인증 정보 입니다.", 404);

    private final String message;
    private final Integer code;
}
