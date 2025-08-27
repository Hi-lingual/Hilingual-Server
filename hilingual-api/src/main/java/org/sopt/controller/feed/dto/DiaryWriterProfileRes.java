package org.sopt.controller.feed.dto;

import org.sopt.diary.domain.Diary;
import org.sopt.userprofile.domain.UserProfile;

import java.time.Duration;
import java.time.LocalDateTime;

public record DiaryWriterProfileRes(
        Boolean isMine,
        Profile profile,
        FeedDiary diary
) {
    public static DiaryWriterProfileRes of(
            final Diary diary,
            final boolean isMine,
            final boolean isLikedByUser) {
        return new DiaryWriterProfileRes(
                isMine,
                Profile.of(diary.getUser().getUserProfile()),
                FeedDiary.of(diary, isLikedByUser)
        );
    }
    record Profile(
            Long userId,
            String profileImg,
            String nickname,
            Integer streak
    ){
        static Profile of(final UserProfile userProfile) {
            return new Profile(
                    userProfile.getUser().getId(),
                    (userProfile.getProfileImg() != null) ? userProfile.getProfileImg() : "",
                    userProfile.getNickname(),
                    userProfile.getStreak()
            );
        }
    }

    record FeedDiary(
            Long sharedDate,
            Integer likeCount,
            Boolean isLiked
    ){
        static FeedDiary of(final Diary diary, final boolean isLikedByUser) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = diary.getCreatedAt();
            long minutesDiff = Duration.between(createdAt, now).toMinutes();

            return new FeedDiary(
                    (minutesDiff < 1) ? 0L : minutesDiff,
                    diary.getIsLiked(),
                    isLikedByUser
            );
        }
    }
}
