package org.hilingual.domain.voca.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.hilingual.domain.voca.api.exception.VocaInvalidSortTypeException;


import java.util.List;

@Component
@RequiredArgsConstructor
public class VocaRetriever {

    private final VocaRepository vocaRepository;
    private final VocaGroupFactory vocaGroupFactory;

    public VocaListResponse findGroupedVoca(final Long userId, final int sort) {
        final List<Voca> vocas = switch (sort) {
            case 1 -> vocaRepository.findAllByUserIdOrderByPhraseAsc(userId);
            case 2 -> vocaRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
            default -> throw new VocaInvalidSortTypeException();
        };

        return vocaGroupFactory.create(vocas, sort);
    }

}
