package com.myanime.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileUtil {
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    private FileUtil() {
    }

    public static String readFromFile(String path) {
        if (CACHE.containsKey(path)) {
            return CACHE.get(path);
        }

        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        String result = text.toString();
        CACHE.put(path, result);
        return result;
    }
}
