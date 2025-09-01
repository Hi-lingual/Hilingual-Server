package org.sopt.likeddiary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.likeddiary.domain.LikedDiary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedDiaryFacade {

    private final LikedDiaryRetriever likedDiaryRetriever;

    public List<Long> findLikedDiaryIdsByUserIdAndDiaryIdsIn(Long userId, List<Long> diaryIds) {
        return likedDiaryRetriever.findLikedDiaryIdsByUserIdAndDiaryIdsIn(userId, diaryIds);
    }

    public List<LikedDiary> getLikedDiariesWithAllDetails(Long userId) {
        return likedDiaryRetriever.getLikedDiariesWithAllDetails(userId);
    }

    public Boolean findUserAndDiaryExist(Long userId, Long diaryId) {
        return likedDiaryRetriever.findUserAndDiaryExist(userId, diaryId);
    }
}
