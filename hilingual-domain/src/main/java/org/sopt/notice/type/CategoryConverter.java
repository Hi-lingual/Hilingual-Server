package org.sopt.notice.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.sopt.feedalarm.type.TargetType;

@Converter(autoApply = false)
public class CategoryConverter implements AttributeConverter<Category, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Category attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public Category convertToEntityAttribute(Integer dbData) {
        return dbData != null ? Category.fromCode(dbData) : null;
    }
}
