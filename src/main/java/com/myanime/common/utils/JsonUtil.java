package com.myanime.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {
    private JsonUtil() {
        // Private constructor to prevent instantiation
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
    }

    public static String toString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T toObject(Object o, Class<T> clazz) {
        try {
            if (o == null) {
                return null;
            }
            if (o instanceof String) {
                return mapper.readValue((String) o, clazz);
            }
            String str = mapper.writeValueAsString(o);
            return mapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T jsonToObject(String str, TypeReference<T> type) {
        try {
            return mapper.readValue(str, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> toList(String str, Class<T> clazz) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> toList(Object o, Class<T> clazz) {
        try {
            if (o == null) {
                return null;
            }
            if (o instanceof String) {
                JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
                return mapper.readValue((String) o, javaType);
            }

            JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            String str = mapper.writeValueAsString(o);
            return mapper.readValue(str, javaType);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T objectToJson(Object o, TypeReference<T> type) {
        String jsonStr = toString(o);

        if (jsonStr == null) {
            return null;
        }


        return jsonToObject(jsonStr, type);
    }

    public static String readField(String json, String key) {
        if (StringUtils.hasText(json) && StringUtils.hasText(key)) {
            try {
                ObjectNode object = new ObjectMapper().readValue(json, ObjectNode.class);
                JsonNode node = object.get(key);
                return (node == null ? null : mapper.writeValueAsString(node));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    public static boolean isJsonValid(String jsonInString) {
        try {
            mapper.readTree(jsonInString);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static JsonNode getJsonNode(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }

        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String unsetFields(String json, List<String> keys) {
        Map<String, Object> objectMap = jsonToObject(json, new TypeReference<Map<String, Object>>() {
        });
        if (objectMap == null) {
            return null;
        }

        for (String key : keys) {
            objectMap.remove(key);
        }

        return toString(objectMap);
    }

    public static Object getValueFromJsonPath(Map<String, Object> contextData, String jsonPath) {
        if (contextData == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert map to JSON string
            String json = mapper.writeValueAsString(contextData);

            // Parse and extract value
            return JsonPath.read(json, jsonPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Sentry.captureException(e);
            return null;
        }
    }
}

