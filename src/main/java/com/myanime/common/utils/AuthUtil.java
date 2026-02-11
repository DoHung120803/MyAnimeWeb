package com.myanime.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {
    }

    public static String getCurrentUserId() {
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
}
