package org.hilingual.domain.diary.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryRemover {

    private final DiaryRepository diaryRepository;

}