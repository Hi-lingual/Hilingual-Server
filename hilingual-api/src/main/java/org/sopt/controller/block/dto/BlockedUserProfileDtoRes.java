package org.sopt.controller.block.dto;

import org.sopt.userprofile.domain.UserProfile;

public record BlockedUserProfileDtoRes(
        Long userId,
        String profileImg,
        String nickname
){
    public static BlockedUserProfileDtoRes from(UserProfile profile) {
        return new BlockedUserProfileDtoRes(
                profile.getUser().getId(),
                profile.getProfileImg(),
                profile.getNickname()
        );
    }
}