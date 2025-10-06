package com.github.lunasis.global.exception;

public interface ApiExceptions {

    Integer getCode();

    String getMessage();

    default ApiException toApiException() {

        return new ApiException(this);
    }
}
