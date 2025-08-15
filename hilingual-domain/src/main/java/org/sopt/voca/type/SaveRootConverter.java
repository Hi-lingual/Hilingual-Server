package org.sopt.voca.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.sopt.user.type.RegisterStatus;

@Converter(autoApply = false)
public class SaveRootConverter implements AttributeConverter<SaveRoot, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SaveRoot attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public SaveRoot convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return SaveRoot.fromCode(dbData);
    }
}
