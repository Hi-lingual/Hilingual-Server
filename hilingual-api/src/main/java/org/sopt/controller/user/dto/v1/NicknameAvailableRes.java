package org.sopt.controller.user.dto.v1;

public record NicknameAvailableRes(
        Boolean isAvailable
) {
    public static NicknameAvailableRes from(Boolean availability) {
        return new NicknameAvailableRes(availability);
    }
}
