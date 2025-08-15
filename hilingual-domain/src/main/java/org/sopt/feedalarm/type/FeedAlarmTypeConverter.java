package org.sopt.feedalarm.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class FeedAlarmTypeConverter implements AttributeConverter<FeedAlarmType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FeedAlarmType attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public FeedAlarmType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? FeedAlarmType.fromCode(dbData) : null;
    }
}
