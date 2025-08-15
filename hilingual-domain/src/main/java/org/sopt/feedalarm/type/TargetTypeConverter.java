package org.sopt.feedalarm.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TargetTypeConverter implements AttributeConverter<TargetType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TargetType attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public TargetType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? TargetType.fromCode(dbData) : null;
    }
}
