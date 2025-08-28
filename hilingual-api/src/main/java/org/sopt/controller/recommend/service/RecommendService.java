package org.sopt.controller.recommend.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.recommend.dto.RecommendListRes;
import org.sopt.controller.recommend.exception.RecommendApiErrorCode;
import org.sopt.controller.recommend.exception.RecommendForbiddenException;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.facade.RecommendFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.voca.facade.VocaRemover;
import org.sopt.voca.facade.VocaSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {

    private final DiaryFacade diaryFacade;
    private final UserFacade userFacade;
    private final RecommendFacade recommendFacade;

    private final VocaRemover vocaRemover;
    private final VocaSaver vocaSaver;


    @Transactional
    public void saveRecommend(Recommend recommend) {
        recommendFacade.save(recommend);
    }

    public RecommendListRes getRecommendList(final long userId, final long diaryId){
        diaryFacade.validateReadable(userId, diaryId);

        List<RecommendListRes.PhraseDto> phrases = recommendFacade.findByDiaryId(diaryId).stream()
                .map(r -> new RecommendListRes.PhraseDto(
                        r.getId(),
                        parsePhraseType(r.getPhraseType()),
                        r.getPhrase(),
                        r.getExplanation(),
                        r.getReason(),
                        r.getIsBookmarked()
                ))
                .toList();
        return new RecommendListRes(phrases);
    }

    @Transactional
    public Void bookMark(final long userId, final long phraseId, final boolean isBookmarked) {

        if (!isBookmarked) {
            vocaRemover.delete(userId, phraseId);
            return null;
        }

        Recommend recommend = recommendFacade.findById(phraseId);
        Diary diary = recommend.getDiary();
        boolean mine = (diary != null) && userId == diary.getUser().getId();
        boolean publicDiary = (diary == null) || Boolean.TRUE.equals(diary.getIsPublic());

        if (!mine && !publicDiary) {
            throw new RecommendForbiddenException(RecommendApiErrorCode.RECOMMEND_FORBIDDEN);
        }

        User user = userFacade.getUserById(userId);
        vocaSaver.saveIfNotExists(user, recommend);
        return null;
    }

    private List<String> parsePhraseType(String phraseType) {
        if (phraseType == null || phraseType.isBlank()) {
            return List.of();
        }
        return Arrays.stream(phraseType.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}