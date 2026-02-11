package com.myanime.common.constants;

import com.myanime.domain.enums.NotifyType;

import java.util.List;

public class GlobalConstant {
    private GlobalConstant() {
        // Private constructor to prevent instantiation
    }

    public static final List<Short> SUPPORTED_NOTIFY_TYPES = List.of(
//            NotifyType.EMAIL.getValue(),
//            NotifyType.TELEGRAM.getValue(),
            NotifyType.IN_APP.getValue()
    );
}
