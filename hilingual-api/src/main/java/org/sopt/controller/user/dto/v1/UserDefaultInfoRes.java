package org.sopt.controller.user.dto.v1;

public record UserDefaultInfoRes(
        String profileImg,
        String nickname,
        String provider,
        Long userId
) {
    public static UserDefaultInfoRes from(final String nickname, final String provider, final String profileImg, final long userId) {
        return new UserDefaultInfoRes(
                (profileImg != null) ? profileImg : " ",
                nickname,
                provider,
                userId
        );
    }
}
