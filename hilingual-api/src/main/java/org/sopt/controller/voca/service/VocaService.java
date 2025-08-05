package org.sopt.controller.voca.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.voca.dto.res.VocaDetailResponse;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.controller.voca.dto.res.VocaSearchListResponse;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.facade.RecommendFacade;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.facade.VocaRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;
    private final RecommendFacade recommendFacade;


    // 단어장 목록 조회
    public VocaListRes getVocaList(final Long userId, final int sort) {
        return vocaRetriever.findGroupedVoca(userId, sort);
    }

    // 단어장 검색
    public VocaSearchListResponse searchVocaList(final Long userId, final String keyword) {
        final List<Voca> vocas = vocaRetriever.findStartsWithVoca(userId, keyword);
        return VocaSearchListResponse.from(vocas);
    }

    //특정 단어 세부 조회
    public VocaDetailResponse getVocaDetails(final Long userId, final Long phraseId) {
        final Recommend recommend = recommendFacade.findByUserIdAndPhraseId(userId, phraseId);
        return VocaDetailResponse.from(recommend);
    }

}
