package com.myanime.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    ANIME_EXISTED(1001, "Anime existed!", HttpStatus.BAD_REQUEST),
    ANIME_NAME_INVALID(1002, "Anime's name must be at least 3 character!", HttpStatus.BAD_REQUEST),
    ANIME_IFRAME_INVALID(1003, "Anime's iframe must not be empty", HttpStatus.BAD_REQUEST),
    ANIME_THUMBNAIL_INVALID(1004, "Anime's thumbnail must not be empty", HttpStatus.BAD_REQUEST),
    ANIME_NOT_FOUND(1005, "Anime not found!", HttpStatus.NOT_FOUND),

    USER_EXISTED(1001, "User existed!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "User's name must be at least 3 characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found!", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission!", HttpStatus.FORBIDDEN),

    PERMISSION_NOT_FOUND(1005, "Permission not found!", HttpStatus.NOT_FOUND),

    INVALID_KEY(1005, "Invalid message key", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode (int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
