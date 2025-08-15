package org.sopt.notice.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CategoryConverter implements AttributeConverter<Category, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Category attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public Category convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Category.fromCode(dbData);
    }
}
