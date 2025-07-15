package org.hilingual.domain.userprofile.core.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.facade.DiaryRetriever;
import org.hilingual.domain.userprofile.core.domain.UserProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUpdater {

    private final UserProfileRetriever userProfileRetriever;
    private final DiaryRetriever diaryRetriever;

    private static final long STREAK_WINDOW_HOURS = 48L;

    @Transactional
    public void updateDiaryStats(final Long userId) {
        UserProfile profile = userProfileRetriever.findByUserIdOrThrow(userId);

        List<LocalDateTime> diaryTimestamps = diaryRetriever.findDiaryCreatedAts(userId);

        profile.updateTotalDiaries(diaryTimestamps.size());

        LocalDateTime now = LocalDateTime.now();

        if (hasWrittenInLast48Hours(diaryTimestamps, now)) {
            profile.updateStreak(profile.getStreak() + 1);
        } else {
            profile.updateStreak(1); // streak 리셋
        }
    }

    private boolean hasWrittenInLast48Hours(List<LocalDateTime> diaryTimestamps, LocalDateTime now) {
        return diaryTimestamps.stream()
                .anyMatch(writtenAt -> writtenAt.isAfter(now.minusHours(STREAK_WINDOW_HOURS)));
    }
}
