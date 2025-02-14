package com.myanime.exception;

import com.myanime.model.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    ApiResponse apiResponse = new ApiResponse();

    @ExceptionHandler(value =  Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(Exception exception) {

        log.info("Exception: ", exception);

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
    ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException exception) {
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

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException() {
        ErrorCode errorCode = ErrorCode.REQUEST_BODY_EMPTY;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    ResponseEntity<ApiResponse<Object>> handleDataAccessResourceFailureException() {
        ErrorCode errorCode = ErrorCode.ELASTICSEARCH_CONNECTION_ERROR;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = RedisConnectionFailureException.class)
    ResponseEntity<ApiResponse<Object>> handleDRedisConnectionFailureException() {
        ErrorCode errorCode = ErrorCode.REDIS_CONNECTION_ERROR;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException() {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_PARAM;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MultipartException.class)
    ResponseEntity<ApiResponse<Object>> handleMultipartException() {
        ErrorCode errorCode = ErrorCode.FILE_EMPTY;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceededException () {
        ErrorCode errorCode = ErrorCode.FILE_SIZE_INVALID;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    private void setApiResponse(int code, String message) {
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
    }
}
