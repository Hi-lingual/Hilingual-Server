package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.repository.DiaryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryRemover {

    private final DiaryRepository diaryRepository;

    public void deleteAllByUserId(final Long userId) {
        diaryRepository.deleteAllByUserId(userId);
    }

}