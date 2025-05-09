package com.rich.sol_bot.system.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class JacksonOperator {
    private final ObjectMapper objectMapper;

    public JacksonOperator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows(JsonProcessingException.class)
    public String writeValueAsString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T readValue(String content, Class<T> clazz) {
        return objectMapper.readValue(content, clazz);
    }

    @SneakyThrows
    public <T> T readValue(String content, TypeReference<T> typeReference) {
        return objectMapper.readValue(content, typeReference);
    }

}
