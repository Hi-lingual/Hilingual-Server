package org.sopt.controller.feed.dto;


import org.sopt.diary.domain.Diary;
import org.sopt.userprofile.domain.UserProfile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public record SharedDiaryListRes(
        Profile profile,
        List<SharedDiary> diaryList
) {
    public static SharedDiaryListRes of(final UserProfile userProfile, final List<Diary> diaries, final List<Boolean> isLikedByUser) {
        final List<SharedDiary> sharedDiaries = IntStream.range(0, diaries.size())
                .mapToObj(i -> SharedDiary.of(diaries.get(i), isLikedByUser.get(i)))
                .toList();

        return new SharedDiaryListRes(Profile.from(userProfile), sharedDiaries);
    }

    record Profile(
            String profileImg,
            String nickname
    ) {
        static Profile from(final UserProfile userProfile) {
            return new Profile(
                    (userProfile.getProfileImg() != null) ? userProfile.getProfileImg() : " ",
                    userProfile.getNickname()
            );
        }
    }
    }

    record SharedDiary(
            Long diaryId,
            Long sharedDate,
            Integer likeCount,
            Boolean isLiked,
            String diaryImg,
            String originalText
    ) {
        static SharedDiary of(final Diary diary, final boolean isLikedByUser) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = diary.getCreatedAt();
            long minutesDiff = Duration.between(createdAt, now).toMinutes();

            return new SharedDiary(
                    diary.getId(),
                    (minutesDiff < 1) ? 0L : minutesDiff,
                    diary.getIsLiked(),
                    isLikedByUser,
                    (diary.getImageUrl() != null) ? diary.getImageUrl() : " ",
                    diary.getOriginalText()
            );
    }
}
