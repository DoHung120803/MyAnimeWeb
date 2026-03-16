package com.myanime.infrastructure.configurations.securities.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {
    }

    public static CustomUserDetails getAuthUserDetails() {
        return (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getCurrentUserId() {
        return getAuthUserDetails().getId();
    }
}
