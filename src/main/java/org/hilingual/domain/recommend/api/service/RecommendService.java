package org.hilingual.domain.recommend.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.facade.DiaryRetriever;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryValidator;
import org.hilingual.domain.recommend.api.dto.res.RecommendList;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.core.facade.RecommendRetriever;
import org.hilingual.domain.recommend.core.facade.RecommendSaver;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.facade.UserRetriever;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.facade.VocaRemover;
import org.hilingual.domain.voca.core.facade.VocaRetriever;
import org.hilingual.domain.voca.core.facade.VocaSaver;
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
    private final VocaRetriever vocaRetriever;
    private final VocaRemover vocaRemover;
    private final VocaSaver vocaSaver;
    private final UserRetriever userRetriever;

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

    @Transactional(readOnly = false)
    public Void bookMark(final long userId, final long phraseId, final boolean isBookMarked){
        Recommend recommend = recommendRetriever.findById(phraseId);
        User user = userRetriever.findByUserId(userId);
        recommend.updateMarkStatus(isBookMarked);

        if (isBookMarked) {
            Voca voca = new Voca(user, recommend);
            vocaSaver.saveIfNotExists(voca);
        } else {
            vocaRemover.delete(user, recommend);
        }

        return null;
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