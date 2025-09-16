package org.sopt.controller.voca.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.voca.dto.res.VocaDetailResponse;
import org.sopt.recommend.domain.Recommend;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.controller.voca.dto.res.VocaSearchListResponse;
import org.sopt.recommend.facade.RecommendFacade;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.facade.VocaFacade;
import org.sopt.voca.facade.VocaRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;
    private final RecommendFacade recommendFacade;
    private final VocaFacade vocaFacade;

    // 단어장 목록 조회
    public VocaListRes getVocaList(final Long userId, final int sort) {
        return vocaRetriever.findGroupedVoca(userId, sort);
    }

    // 단어장 검색
    public VocaSearchListResponse searchVocaList(final Long userId, final String keyword) {
        final List<Voca> vocas = vocaRetriever.findStartsWithVoca(userId, keyword);

        // ✅ 검색 시 무조건 A–Z
        vocas.sort(Comparator.comparing(v -> v.getPhrase().toLowerCase(Locale.ROOT)));

        return VocaSearchListResponse.from(vocas);
    }

    // 특정 단어 세부 조회
    public VocaDetailResponse getVocaDetails(Long userId, Long recommendId) {
        return vocaFacade.findOptionalByUserIdAndRecommendId(userId, recommendId)
                .map(v -> VocaDetailResponse.ofSnapshot(v, true))
                .orElseGet(() -> {
                    Recommend recommend = recommendFacade.findByIdWithDiary(recommendId);
                    String writtenFrom = buildWrittenFrom(userId, recommend, false);
                    return VocaDetailResponse.of(recommend, writtenFrom, false);
                });
    }

    private String buildWrittenFrom(Long userId, Recommend rec, boolean isBookmarked) {
        if (isBookmarked) {
            return vocaFacade.findOptionalByUserIdAndRecommendId(userId, rec.getId())
                    .map(v -> {
                        return switch (v.getSavedRoot()) {
                            case FEED    -> "피드에서 저장됨";
                            case MY      -> v.getWrittenFrom();
                            case UNKNOWN -> "알 수 없는 출처";
                        };
                    })
                    .orElseGet(() -> fallbackFromRecommend(userId, rec));
        }
        // 북마크 해제 상태: Recommend 출처로 유추
        return fallbackFromRecommend(userId, rec);
    }

    private String fallbackFromRecommend(Long userId, Recommend rec) {
        Long ownerId = rec.getDiary().getUser().getId();
        if (ownerId.equals(userId)) {
            var date = rec.getDiary().getWrittenDate();
            var fmt  = java.time.format.DateTimeFormatter.ofPattern("yy.MM.dd");
            return date.format(fmt) + " 일기에서 저장됨";
        }
        return "피드에서 저장됨";
    }
}
