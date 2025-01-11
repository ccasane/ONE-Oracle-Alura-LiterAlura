package com.one.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.literalura.repository.inDataConvert;

public class DataConvert implements inDataConvert {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T dataConvert(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
