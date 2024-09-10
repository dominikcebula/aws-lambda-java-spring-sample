package com.dominikcebula.samples.aws.lambda.products.catalog.shared.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;

    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readerFor(type).readValue(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to read object of type %s from json.".formatted(ClassUtils.getName(type)), e);
        }
    }

    public <T> List<T> listFromJson(String json, Class<T> type) {
        try {
            return objectMapper.readerForListOf(type).readValue(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to read object of type %s from json.".formatted(ClassUtils.getName(type)), e);
        }
    }

    public String asJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to convert object of type %s to json.".formatted(ClassUtils.getName(obj)), e);
        }
    }
}
