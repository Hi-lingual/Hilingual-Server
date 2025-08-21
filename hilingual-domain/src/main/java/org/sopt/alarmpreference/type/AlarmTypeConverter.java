package org.sopt.alarmpreference.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AlarmTypeConverter implements AttributeConverter<AlarmType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AlarmType attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public AlarmType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? AlarmType.fromCode(dbData) : null;
    }
}
