package org.sopt.controller.feed.dto;


import org.sopt.diary.domain.Diary;
import org.sopt.userprofile.domain.UserProfile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.sopt.controller.feed.dto.FeedDtoConstants.*;

public record SharedDiaryListRes(
        Profile profile,
        List<SharedDiary> diaryList
) {
    public static SharedDiaryListRes of(final UserProfile userProfile, final String profileImgUrl, final List<SharedDiary> sharedDiaries) {
        return new SharedDiaryListRes(Profile.from(userProfile, profileImgUrl), sharedDiaries);
    }

    public record Profile(
            String profileImg,
            String nickname
    ) {
        public static Profile from(final UserProfile userProfile, final String profileImgUrl) {
            return new Profile(
                    (profileImgUrl != null) ? profileImgUrl : " ", // 변환된 URL 사용
                    userProfile.getNickname()
            );
        }
    }

    public record SharedDiary(
            Long diaryId,
            Long sharedDate,
            Integer likeCount,
            Boolean isLiked,
            String diaryImg,
            String originalText
    ) {
        public static SharedDiary of(final Diary diary, final boolean isLikedByUser, final String diaryImgUrl) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime sharedTime = diary.getSharedTime();
            long minutesDiff = Duration.between(sharedTime, now).toMinutes();

            return new SharedDiary(
                    diary.getId(),
                    (minutesDiff < ONE_MINUTE) ? LESS_THAN_MINUTE : minutesDiff,
                    diary.getIsLiked(),
                    isLikedByUser,
                    diaryImgUrl, // 변환된 URL 사용
                    diary.getOriginalText()
            );
        }
    }
}