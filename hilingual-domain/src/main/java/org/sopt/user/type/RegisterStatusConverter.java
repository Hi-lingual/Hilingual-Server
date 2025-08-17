package org.sopt.user.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.sopt.feedalarm.type.TargetType;

@Converter(autoApply = false)
public class RegisterStatusConverter implements AttributeConverter<RegisterStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RegisterStatus attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public RegisterStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? RegisterStatus.fromCode(dbData) : null;
    }
}
