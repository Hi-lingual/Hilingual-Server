package org.sopt.userprofile.dto;

public record UserSearchDto(
        Long userId,
        String profileImg,
        String nickname,
        Boolean isFollowing,
        Boolean isFollowed
) {
}
