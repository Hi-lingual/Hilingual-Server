package org.hilingual.domain.voca.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.repository.VocaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VocaSearcher {

    private final VocaRepository vocaRepository;

    public List<Voca> searchStartsWith(final Long userId, final String keyword) {
        return vocaRepository.findAllByUserIdAndPhraseStartsWith(userId, keyword);
    }
}
