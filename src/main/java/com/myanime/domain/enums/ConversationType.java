package com.myanime.domain.enums;

import com.myanime.common.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConversationType {
    DIRECT((short) 1, "Direct"),
    GROUP((short) 2, "Group");

    private final Short type;
    private final String name;

    public static ConversationType fromType(Short type) throws BadRequestException {
        for (ConversationType conversationType : values()) {
            if (conversationType.getType().equals(type)) {
                return conversationType;
            }
        }
        throw new BadRequestException("Type không hợp lệ");
    }

}
