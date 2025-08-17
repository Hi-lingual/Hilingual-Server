package org.sopt.notice.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.notice.exception.InvalidCategoryTypeException;
import org.sopt.notice.exception.NoticeCoreErrorCode;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    NOTIFICATION(1),
    MARKETING(2);

    private final int code;

    public static Category fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new InvalidCategoryTypeException(NoticeCoreErrorCode.INVALID_CATEGORY_TYPE));
    }
}
