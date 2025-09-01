package org.sopt.userprofile.dto;

public interface UserSearchProjection {
    Long getUserId();
    String getProfileImg();
    String getNickname();
    Boolean getIsFollowing();
    Boolean getIsFollowed();
}
