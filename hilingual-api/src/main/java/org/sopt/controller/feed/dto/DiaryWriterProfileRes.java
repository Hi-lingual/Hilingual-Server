package org.sopt.controller.feed.dto;

import org.sopt.diary.domain.Diary;
import org.sopt.userprofile.domain.UserProfile;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.sopt.controller.feed.dto.FeedDtoConstants.*;

public record DiaryWriterProfileRes(
        Boolean isMine,
        Profile profile,
        FeedDiary diary
) {
    public static DiaryWriterProfileRes of(
            final Diary diary,
            final boolean isMine,
            final boolean isLikedByUser,
            final String profileImgUrl
    ) {
        return new DiaryWriterProfileRes(
                isMine,
                Profile.of(diary.getUser().getUserProfile(), profileImgUrl),
                FeedDiary.of(diary, isLikedByUser)
        );
    }
    record Profile(
            Long userId,
            String profileImg,
            String nickname,
            Integer streak
    ){
        static Profile of(final UserProfile userProfile, final String profileImgUrl) {
            return new Profile(
                    userProfile.getUser().getId(),
                    (profileImgUrl != null) ? profileImgUrl : " ",
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
            LocalDateTime sharedAt = diary.getSharedTime();
            long minutesDiff = Duration.between(sharedAt, now).toMinutes();

            return new FeedDiary(
                    (minutesDiff < ONE_MINUTE) ? LESS_THAN_MINUTE : minutesDiff,
                    diary.getIsLiked(),
                    isLikedByUser
            );
        }
    }
}
