package org.hilingual.domain.user.api.dto.res;

public record NicknameAvailableResponse(
        Boolean isAvailable
) {
    public static NicknameAvailableResponse from(Boolean availability) {
        return new NicknameAvailableResponse(availability);
    }
}
