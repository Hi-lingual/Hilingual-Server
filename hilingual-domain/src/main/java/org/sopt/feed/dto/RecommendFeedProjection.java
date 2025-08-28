package org.sopt.feed.dto;

import java.time.LocalDateTime;

public interface RecommendFeedProjection {
    // UserProfile
    Long getUserId();
    Boolean getIsMine();
    String getProfileImg();
    String getNickname();
    Integer getStreak();

    // Diary
    Long getDiaryId();
    LocalDateTime getSharedDate();
    Integer getLikeCount();
    String getDiaryImg();
    String getOriginalText();

    // LikedDiary
    Boolean getIsLiked();
}