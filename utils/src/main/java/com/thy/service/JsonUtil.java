package com.thy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static <T> T deserialize(Map<String, Object> sourceMap, Class<T> clazz) {
        try {
            String json = objectMapper.writeValueAsString(sourceMap);
            return deserialize(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing map to object: {}", e.getMessage());
            return null;
        }
    }


    public static <T> List<T> deserializeToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static JsonNode readTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static Map convertToMap(Object data) {
        try {
            return objectMapper.convertValue(data, Map.class);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
