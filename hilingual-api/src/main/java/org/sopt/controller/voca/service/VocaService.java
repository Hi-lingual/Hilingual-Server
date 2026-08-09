package org.sopt.controller.voca.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.voca.dto.req.VocaMemorizationUpdateRequest;
import org.sopt.controller.voca.dto.res.VocaDetailResponse;
import org.sopt.controller.voca.exception.VocaApiErrorCode;
import org.sopt.controller.voca.exception.VocaSourceNotFoundException;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.exception.RecommendNotFoundException;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.controller.voca.dto.res.VocaSearchListResponse;
import org.sopt.recommend.facade.RecommendFacade;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.facade.VocaFacade;
import org.sopt.voca.facade.VocaRetriever;
import org.sopt.voca.type.SavedRoot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;
    private final RecommendFacade recommendFacade;
    private final VocaFacade vocaFacade;

    // 단어장 목록 조회
    public VocaListRes getVocaList(final Long userId, final int sort, final boolean unmemorizedOnly) {
        return vocaRetriever.findGroupedVoca(userId, sort, unmemorizedOnly);
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
                .map(v -> {
                    // 북마크된 경우
                    String writtenDate = ((v.getSavedRoot() == SavedRoot.MY) && (v.getWrittenDate() != null)) ? v.getWrittenDate().toString() : null;

                    return VocaDetailResponse.ofSnapshot(v, writtenDate, true);
                })
                .orElseGet(() -> {
                    // 북마크되지 않은 경우
                    Recommend recommend;
                    try{
                        recommend = recommendFacade.findByIdWithDiary(recommendId);
                    } catch (RecommendNotFoundException e) {
                        throw new VocaSourceNotFoundException(VocaApiErrorCode.VOCA_SOURCE_NOT_FOUND);
                    }

                    SavedRoot root = determineSavedRoot(userId, recommend);

                    String writtenFrom = buildWrittenFrom(userId, recommend, false);

                    String writtenDate = null;
                    if (root == SavedRoot.MY) {
                        writtenDate = recommend.getDiary().getWrittenDate().toString();
                    }

                    return VocaDetailResponse.of(recommend, writtenFrom, writtenDate, root, false);
                });
    }

    private SavedRoot determineSavedRoot(Long userId, Recommend rec) {
        Long ownerId = rec.getDiary().getUser().getId();
        return ownerId.equals(userId) ? SavedRoot.MY : SavedRoot.FEED;
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

    @Transactional
    public void updateMemorization(final Long userId, final VocaMemorizationUpdateRequest request){
        final Map<Long, Boolean> memorizationByRecommendId = request.items().stream()
                .collect(Collectors.toMap(
                        VocaMemorizationUpdateRequest.Item::phraseId,
                        VocaMemorizationUpdateRequest.Item::isMemorized,
                        (existing, replacement) -> replacement
                ));
        vocaFacade.updateMemorization(userId, memorizationByRecommendId);
    }
}
