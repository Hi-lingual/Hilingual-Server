package org.sopt.likeddiary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.likeddiary.repository.LikedDiaryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedDiaryRetriever {

    private final LikedDiaryRepository likedDiaryRepository;

    public List<Long> findLikedDiaryIdsByUserIdAndDiaryIdsIn(Long userId, List<Long> diaryIds) {
        return likedDiaryRepository.findLikedDiaryIdsByUserIdAndDiaryIdsIn(userId, diaryIds);
    }
}
