package com.myanime.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),

    ANIME_EXISTED(1001, "Anime existed!"),
    ANIME_NAME_INVALID(1002, "Anime's name must be at least 3 character!"),
    ANIME_IFRAME_INVALID(1003, "Anime's iframe must not be empty"),
    ANIME_THUMBNAIL_INVALID(1004, "Anime's thumbnail must not be empty"),
    ANIME_NOT_FOUND(1005, "Anime not found!"),

    USER_EXISTED(1001, "User existed!"),
    USERNAME_INVALID(1002, "User's name must be at least 3 characters!"),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters"),
    USER_NOT_FOUND(1004, "User not found!"),

    INVALID_KEY(1005, "Invalid message key"),
    ;

    private int code;
    private String message;
}
