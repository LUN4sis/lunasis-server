package com.github.lunasis.global.dto;

import com.github.lunasis.global.exception.ApiException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final int code;
    private final T data;

    public static <T> ApiResponse<T> ok(T data) {

        return new ApiResponse<>(true, null, 200, data);
    }

    public static ApiResponse<Void> ok() {
        return ok(null);
    }

    public static ApiResponse<Void> error(String message, int code) {

        return new ApiResponse<>(false, message, code, null);
    }

    public static ApiResponse<Void> error(ApiException exception) {

        return error(exception.getMessage(), exception.getCode());
    }

}
