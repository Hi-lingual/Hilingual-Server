package org.sopt.controller.user.dto;

public record UserDefaultInfoRes(
        String profileImg,
        String nickname,
        String provider
) {
    public static UserDefaultInfoRes from(final String nickname, final String provider, final String profileImg) {
        return new UserDefaultInfoRes(
                (profileImg != null) ? profileImg : " ",
                nickname,
                provider
        );
    }
}
