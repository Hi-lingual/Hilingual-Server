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

    public List<LikedDiary> findLikedDiariesWithDetailsByUserId(Long userId) {
        return likedDiaryRetriever.findLikedDiariesWithDetailsByUserId(userId);
    }
}
