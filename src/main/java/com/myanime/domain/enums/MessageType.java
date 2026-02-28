package com.myanime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT((short) 1, "text"),
    MEDIA((short) 2, "media");

    private final Short type;
    private final String description;

    public static MessageType fromType(Short type) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getType().equals(type)) {
                return messageType;
            }
        }
        return null;
    }
}
