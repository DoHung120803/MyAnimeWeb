package com.myanime.domain.enums;

import lombok.Getter;

@Getter
public enum NotifyType {
    EMAIL((short) 1),
    TELEGRAM((short) 2),;

    private final Short value;

    NotifyType(Short value) {
        this.value = value;
    }

}
