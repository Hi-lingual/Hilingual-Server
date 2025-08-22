package org.sopt.controller.feed.dto;


import org.sopt.diary.domain.Diary;
import org.sopt.userprofile.domain.UserProfile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record LikedDiaryListRes(
        List<LikedDiaryDetail> diaryList
) {
    public static LikedDiaryListRes of(final List<LikedDiaryDetail> diaryDetails) {
        return new LikedDiaryListRes(diaryDetails);
    }

    public record LikedDiaryDetail(
        Profile profile,
        LikedDiary diary
    ) {
        public static LikedDiaryDetail of(final Profile profile, final LikedDiary diary) {
            return new LikedDiaryDetail(profile, diary);
        }

        public record Profile(
                Long userId,
                Boolean isMine,
                String profileImg,
                String nickname,
                Integer streak
        ) {
            public static Profile of(final UserProfile userProfile, final boolean isMine) {
                return new Profile(
                        userProfile.getUser().getId(),
                        isMine,
                        (userProfile.getProfileImg() != null) ? userProfile.getProfileImg() : " ",
                        userProfile.getNickname(),
                        userProfile.getStreak()
                );
            }

        }

        public record LikedDiary(
                Long diaryId,
                Long sharedDate,
                Integer likeCount,
                Boolean isLiked,
                String diaryImg,
                String originalText
        ) {
            public static LikedDiary of(final Diary diary) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime createdAt = diary.getCreatedAt();
                long minutesDiff = Duration.between(createdAt, now).toMinutes();

                return new LikedDiary(
                        diary.getId(),
                        (minutesDiff < 1) ? 0L : minutesDiff,
                        diary.getIsLiked(),
                        true,
                        (diary.getImageUrl() != null) ? diary.getImageUrl() : " ",
                        diary.getOriginalText()
                );
            }
        }
    }
}
