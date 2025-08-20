package org.sopt.voca.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SavedRootConverter implements AttributeConverter<SavedRoot, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SavedRoot attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public SavedRoot convertToEntityAttribute(Integer dbData) {
        return dbData != null ? SavedRoot.fromCode(dbData) : null;
    }
}
