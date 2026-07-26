package org.sopt.controller.feed.dto;

import java.util.List;

public record UserListRes(
        List<SearchUser> userList
) {
    public record SearchUser(
            Long userId,
            String profileImg,
            String nickname,
            Boolean isFollowing,
            Boolean isFollowed,
            String externalId
    ){ }
}