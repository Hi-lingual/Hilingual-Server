package org.sopt.likeddiary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.likeddiary.repository.LikedDiaryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikedDiaryRemover {
    private final LikedDiaryRepository likedDiaryRepository;

    @Transactional
    public void deleteByUserIdAndDiaryId(final long userId, final long diaryId) {
        likedDiaryRepository.deleteByUserIdAndDiaryId(userId, diaryId);
    }

    @Transactional
    public void deleteByDiaryId(final long diaryId){
        likedDiaryRepository.deleteByDiaryId(diaryId);
    }

}