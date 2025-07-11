package org.hilingual.domain.voca.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.api.dto.res.VocaSearchListResponse;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.facade.VocaRetriever;
import org.hilingual.domain.voca.core.facade.VocaSearcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hilingual.domain.voca.api.dto.res.VocaSearchResponse;


import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;
    private final VocaSearcher vocaSearcher;


    // 단어장 목록 조회
    public VocaListResponse getVocaList(final Long userId, final int sort) {
        return vocaRetriever.findGroupedVoca(userId, sort);
    }

    // 단어장 검색
    public VocaSearchListResponse searchVocaList(final Long userId, final String keyword) {
        final List<Voca> vocas = vocaSearcher.searchStartsWith(userId, keyword);
        return new VocaSearchListResponse(
                vocas.stream()
                        .map(VocaSearchResponse::from)
                        .toList()
        );
    }
}
