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

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    @Transactional
    public void updateDiaryStats(final Long userId) {
        UserProfile profile = userProfileRetriever.findByUserIdOrThrow(userId);

        int totalDiaries = diaryRetriever.findDiaryCreatedAts(userId).size();
        profile.updateTotalDiaries(totalDiaries);

        LocalDateTime now = LocalDateTime.now(ZONE_ID);

        if (hasWrittenInLast48Hours(userId, now)) {
            profile.updateStreak(profile.getStreak() + 1);
        } else {
            profile.updateStreak(1); // streak 리셋
        }
    }

    private boolean hasWrittenInLast48Hours(Long userId, LocalDateTime now) {
        return diaryRetriever.findDiaryCreatedAts(userId).stream()
                .anyMatch(writtenAt -> writtenAt.isAfter(now.minusHours(48)));
    }
}
