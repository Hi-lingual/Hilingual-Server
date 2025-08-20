package org.sopt.user.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.user.exception.InvalidRegisterStatusException;
import org.sopt.user.exception.UserCoreErrorCode;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RegisterStatus {
    SOCIAL_LOGIN_COMPLETED(1),
    VERIFIED(2),
    PROFILE_COMPLETED(3);

    private final int code;

    public static RegisterStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new InvalidRegisterStatusException(UserCoreErrorCode.INVALID_REGISTER_STATUS_TYPE));
    }
}
