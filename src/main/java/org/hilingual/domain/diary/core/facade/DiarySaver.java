package org.hilingual.domain.diary.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.user.core.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DiarySaver {

    private final DiaryRepository diaryRepository;

    @Transactional
    public Diary save(final User user, final String originalText, final String rewriteText, final String imageUrl) {
        Diary diary = Diary.create(user, originalText, rewriteText, imageUrl);  // 정적 팩토리 메서드 활용
        return diaryRepository.save(diary);
    }

}