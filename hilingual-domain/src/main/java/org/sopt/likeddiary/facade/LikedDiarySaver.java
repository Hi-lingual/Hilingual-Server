package org.sopt.likeddiary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.likeddiary.domain.LikedDiary;
import org.sopt.likeddiary.repository.LikedDiaryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikedDiarySaver {
    private final LikedDiaryRepository likedDiaryRepository;

    @Transactional
    public LikedDiary save(LikedDiary entity) {
        return likedDiaryRepository.save(entity);
    }

}