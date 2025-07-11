package org.hilingual.domain.voca.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.api.exception.VocaApiErrorCode;
import org.hilingual.domain.voca.api.exception.VocaInvalidKoreanKeywordException;
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
            default -> throw new VocaInvalidSortTypeException(VocaApiErrorCode.INVALID_SORT_TYPE);
        };

        return vocaGroupFactory.create(vocas, sort);
    }

    public List<Voca> findStartsWithVoca(final Long userId, final String keyword) {
        if (containsKorean(keyword)) {
            throw new VocaInvalidKoreanKeywordException(VocaApiErrorCode.INVALID_KEYWORD_KOREAN);
        }

        final List<Voca> vocas = vocaRepository.findAllByUserIdAndPhraseStartsWith(userId, keyword);

        if (vocas.isEmpty()) {
            throw new VocaInvalidKoreanKeywordException(VocaApiErrorCode.INVALID_KEYWORD_KOREAN);
        }

        return vocas;
    }

    private boolean containsKorean(final String input) {
        return input != null && input.matches(".*[가-힣]+.*");
    }


}
