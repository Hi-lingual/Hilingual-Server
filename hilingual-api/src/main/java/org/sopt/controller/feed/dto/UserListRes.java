package org.sopt.controller.feed.dto;

import org.sopt.userprofile.dto.UserSearchProjection;

import java.util.List;

public record UserListRes(
        List<SearchUser> userList
) {
    public static UserListRes of(final List<UserSearchProjection> userList) {
        List<SearchUser> searchUserList = userList.stream()
                .map(SearchUser::of)
                .toList();

        return new UserListRes(searchUserList);
    }

    record SearchUser(
            Long userId,
            String profileImg,
            String nickname,
            Boolean isFollowing,
            Boolean isFollowed
    ){
        static SearchUser of(UserSearchProjection userList) {
            return new SearchUser(
                    userList.getUserId(),
                    (userList.getProfileImg() != null) ? userList.getProfileImg() : " ",
                    userList.getNickname(),
                    userList.getIsFollowing(),
                    userList.getIsFollowed()
            );
        }
    }
}