package org.hilingual.domain.recommend.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryValidator;
import org.hilingual.domain.recommend.api.dto.res.RecommendList;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.core.facade.RecommendRetriever;
import org.hilingual.domain.recommend.core.facade.RecommendSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {

    private final RecommendSaver recommendSaver;
    private final RecommendRetriever recommendRetriever;
    private final DiaryValidator diaryValidator;

    @Transactional
    public void saveRecommend(Recommend recommend) {
        recommendSaver.save(recommend);
    }

    public RecommendList getRecommendList(final long userId, final long diaryId){
        diaryValidator.validateDiaryOwnership(userId, diaryId);

        List<RecommendList.PhraseDto> phrases =
                recommendRetriever.findByDiaryId(diaryId).stream()
                        .map(r -> new RecommendList.PhraseDto(
                                r.getId(),
                                parsePhraseType(r.getPhraseType()),
                                r.getPhrase(),
                                r.getExplanation(),
                                r.getReason(),
                                r.getIsMarked()
                        ))
                        .collect(Collectors.toList());
        return new RecommendList(phrases);
    }

    private List<String> parsePhraseType(String phraseType) {
        if (phraseType == null || phraseType.isBlank()) {
            return List.of();
        }
        return Arrays.stream(phraseType.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}