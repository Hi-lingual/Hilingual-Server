package org.sopt.controller.user.dto;

public record UserDefaultInfoRes(
        String profileImg,
        String nickname,
        String provider
) {
}
