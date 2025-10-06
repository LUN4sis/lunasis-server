package com.github.lunasis.global.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final int code;

    public ApiException(String message, Integer code) {

        super(message);
        this.code = code;
    }

    public ApiException(ApiExceptions exceptions) {

        super(exceptions.getMessage());
        this.code = exceptions.getCode();
    }
}
