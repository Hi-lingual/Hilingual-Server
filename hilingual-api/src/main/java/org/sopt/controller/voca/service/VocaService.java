package org.sopt.controller.voca.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.voca.dto.res.VocaDetailResponse;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.controller.voca.dto.res.VocaSearchListResponse;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.facade.VocaFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaFacade vocaFacade;

    // 단어장 목록 조회
    public VocaListRes getVocaList(final Long userId, final int sort) {
        return vocaFacade.findGroupedVoca(userId, sort);
    }

    // 단어장 검색
    public VocaSearchListResponse searchVocaList(final Long userId, final String keyword) {
        final List<Voca> vocas = vocaFacade.findStartsWithVoca(userId, keyword);
        return VocaSearchListResponse.from(vocas);
    }

    //특정 단어 세부 조회
    public VocaDetailResponse getVocaDetails(final Long userId, final Long phraseId) {
        final Voca voca = vocaFacade.findDetailByUserIdAndPhraseId(userId, phraseId);
        return VocaDetailResponse.from(voca);
    }

}
