package org.hilingual.domain.voca.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.core.facade.VocaRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VocaService {

    private final VocaRetriever vocaRetriever;

    public VocaListResponse getVocaList(final Long userId, final int sort) {
        return vocaRetriever.retrieveGroupedVoca(userId, sort);
    }
}
