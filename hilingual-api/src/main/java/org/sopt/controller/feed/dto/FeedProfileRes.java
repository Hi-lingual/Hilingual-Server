package org.sopt.controller.feed.dto;

import org.sopt.userprofile.domain.UserProfile;

public record FeedProfileRes (
        Boolean isMine,
        String profileImg,
        String nickname,
        Integer follower,
        Integer following,
        Integer streak,
        Boolean isFollowing,
        Boolean isFollowed,
        Boolean isBlocked
){
    public static FeedProfileRes from(
            UserProfile profile,
            Boolean isMine,
            Boolean isFollowing,
            Boolean isFollowed,
            Boolean isBlocked,
            String profileImg
    ) {
        return new FeedProfileRes(
                isMine,
                (profileImg != null) ? profileImg : " ",
                profile.getNickname(),
                profile.getFollowerCount(),
                profile.getFollowingCount(),
                profile.getStreak(),
                isFollowing,
                isFollowed,
                isBlocked
        );
    }
}
