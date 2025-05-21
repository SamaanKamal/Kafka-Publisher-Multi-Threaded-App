package org.example.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
