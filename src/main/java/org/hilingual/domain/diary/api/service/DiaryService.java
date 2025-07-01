package org.hilingual.domain.diary.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.facade.DiaryRemover;
import org.hilingual.domain.diary.core.facade.DiaryRetriever;
import org.hilingual.domain.diary.core.facade.DiarySaver;
import org.hilingual.domain.diary.core.facade.DiaryUpdater;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRemover diaryRemover;
    private final DiaryRetriever diaryRetriever;
    private final DiarySaver diarySaver;
    private final DiaryUpdater diaryUpdater;


}
