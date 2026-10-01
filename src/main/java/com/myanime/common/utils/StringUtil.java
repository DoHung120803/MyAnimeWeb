package com.myanime.common.utils;

import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class StringUtil {
    private StringUtil() {
        // Private constructor to prevent instantiation
    }

    private static final String BASE62 =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encodeBase62(UUID uuid) {
        BigInteger value = new BigInteger(
                1,
                ByteBuffer.allocate(16)
                        .putLong(uuid.getMostSignificantBits())
                        .putLong(uuid.getLeastSignificantBits())
                        .array()
        );

        StringBuilder result = new StringBuilder();

        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divide = value.divideAndRemainder(BigInteger.valueOf(62));

            result.append(BASE62.charAt(divide[1].intValue()));
            value = divide[0];
        }

        return result.reverse().toString();
    }

    public static String removeSpaces(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }

        return input.replaceAll("\\s+", "");
    }
}
