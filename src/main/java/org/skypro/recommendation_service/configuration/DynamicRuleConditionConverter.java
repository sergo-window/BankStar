package org.skypro.recommendation_service.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
@Component
public class DynamicRuleConditionConverter implements AttributeConverter<DynamicRuleCondition, String> {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    @Override
    public String convertToDatabaseColumn(DynamicRuleCondition condition) {
        if (condition == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(condition);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting DynamicRuleCondition to JSON", e);
        }
    }

    @Override
    public DynamicRuleCondition convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, DynamicRuleCondition.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to DynamicRuleCondition", e);
        }
    }
}
