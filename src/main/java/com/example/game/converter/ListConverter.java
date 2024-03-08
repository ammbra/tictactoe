package com.example.game.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ListConverter implements AttributeConverter<List<List<String>>, String> {

    private static final Logger logger = LoggerFactory.getLogger(ListConverter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<List<String>> lines) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(lines);
        } catch (final JsonProcessingException e) {
            logger.error("Error when writing JSON", e);
        }

        return json;
    }

    @Override
    public List<List<String>> convertToEntityAttribute(String json) {
        List<List<String>> lines = null;
        try {
            lines = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (final IOException e) {
            logger.error("Error when reading JSON", e);
        }
        return lines;
    }
}