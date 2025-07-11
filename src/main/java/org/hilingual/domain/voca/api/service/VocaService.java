package org.hilingual.domain.voca.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaDetailResponse;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.api.dto.res.VocaSearchListResponse;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.facade.VocaRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;


    // 단어장 목록 조회
    public VocaListResponse getVocaList(final Long userId, final int sort) {
        return vocaRetriever.findGroupedVoca(userId, sort);
    }

    // 단어장 검색
    public VocaSearchListResponse searchVocaList(final Long userId, final String keyword) {
        final List<Voca> vocas = vocaRetriever.findStartsWithVoca(userId, keyword);
        return VocaSearchListResponse.from(vocas);
    }

    //특정 단어 세부 조회
    @Transactional(readOnly = true)
    public VocaDetailResponse getVocaDetail(final Long userId, final Long vocaId) {
        final Voca voca = vocaRetriever.findByUserIdAndVocaId(userId, vocaId);
        return VocaDetailResponse.from(voca);
    }

}
