package com.github.lunasis.global.exception;

public interface ApiExceptions {

    Integer getCode();

    String getMessage();

    default ApiException toException() {

        return new ApiException(this);
    }
}
