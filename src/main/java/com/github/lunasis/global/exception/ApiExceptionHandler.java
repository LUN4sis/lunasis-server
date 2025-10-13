package com.github.lunasis.global.exception;

import com.github.lunasis.global.dto.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        String message = (error != null && error.getDefaultMessage() != null)
                ? error.getDefaultMessage() : "잘못된 요청입니다.";
        return ApiResponse.error(message, 404);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> AccessDeniedException(AccessDeniedException e) {
        return ApiResponse.error("권한이 없습니다.", 403);
    }

    @ExceptionHandler(ApiException.class)
    public ApiResponse<?> handleApiException(ApiException e) {
        return ApiResponse.error(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponse<Void> missingPathVariableException(MissingPathVariableException e) {
        return ApiResponse.error("잘못 된 UUID 입니다", 404);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        return ApiResponse.error("알 수 없는 오류가 발생했습니다.", 500);
    }

}
