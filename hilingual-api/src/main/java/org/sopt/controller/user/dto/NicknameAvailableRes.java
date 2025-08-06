package org.sopt.controller.user.dto;

public record NicknameAvailableRes(
        Boolean isAvailable
) {
    public static NicknameAvailableRes from(Boolean availability) {
        return new NicknameAvailableRes(availability);
    }
}
