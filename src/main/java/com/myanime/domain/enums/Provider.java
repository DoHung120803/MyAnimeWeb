package com.myanime.domain.enums;

import com.myanime.common.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
    GOOGLE("GOOGLE", "Google"),
    FACEBOOK("FACEBOOK", "Facebook"),
    TIKTOK("TIKTOK", "TikTok"),
    YOUTUBE("YOUTUBE",  "YouTube"),;

    private final String type;
    private final String name;

    public static Provider fromType(String type) throws BadRequestException {
        for (Provider conversationType : values()) {
            if (conversationType.getType().equals(type)) {
                return conversationType;
            }
        }
        throw new BadRequestException("Type không hợp lệ");
    }

}
