package org.sopt.user.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RegisterStatusConverter implements AttributeConverter<RegisterStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RegisterStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public RegisterStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return RegisterStatus.fromCode(dbData);
    }
}
