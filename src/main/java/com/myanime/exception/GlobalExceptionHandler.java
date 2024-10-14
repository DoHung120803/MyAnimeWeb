package com.myanime.exception;

import com.myanime.model.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    ApiResponse apiResponse = new ApiResponse();

    @ExceptionHandler(value =  Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {

        int code = (ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        String message = (ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        setApiResponse(code, message);

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        setApiResponse(errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {

        }

        setApiResponse(errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private void setApiResponse(int code, String message) {
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
    }
}
