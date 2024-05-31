package com.myanime.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    ANIME_EXISTED(1001, "Anime existed!"),
    ANIME_NAME_INVALID(1002, "Anime's name must be at least 3 character!"),
    ANIME_IFRAME_INVALID(1002, "Anime's iframe must not be empty"),
    ANIME_THUMBNAIL_INVALID(1002, "Anime's thumbnail must not be empty"),
    INVALID_KEY(1004, "Invalid message key"),
    ;

    private int code;
    private String message;
}
