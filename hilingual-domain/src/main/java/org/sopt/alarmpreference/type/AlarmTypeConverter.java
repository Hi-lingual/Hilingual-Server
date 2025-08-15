package org.sopt.alarmpreference.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AlarmTypeConverter implements AttributeConverter<AlarmType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AlarmType attribute) {
        if (attribute == null) return null;
        return attribute.getCode();
    }

    @Override
    public AlarmType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        return AlarmType.fromCode(dbData);
    }
}
