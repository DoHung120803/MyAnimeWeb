package com.myanime.application.rest.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {
    private ResponseFactory() {

    }

    public static <I> ResponseEntity<ApiResponse<I>> success(I data) {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(true)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<ApiResponse<I>> success() {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<ApiResponse<I>> success(I data, String message) {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(true)
                .message(message)
                .data(data).build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<ApiResponse<I>> error(HttpStatus httpStatus, String message, I data) {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(false)
                .message(message)
                .data(data).build();
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <I> ResponseEntity<ApiResponse<I>> errorWithStatusSuccess(I data, String message) {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(false)
                .message(message)
                .data(data).build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<ApiResponse<I>> response(boolean isSuccess, String message) {
        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(isSuccess)
                .message(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <I> ResponseEntity<ApiResponse<I>> response(I data, boolean isSuccess, String message) {

        ApiResponse<I> response = ApiResponse.<I>builder()
                .success(isSuccess)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }
}
