package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VocaRetriever {

    private final VocaRepository vocaRepository;
    private final VocaGroupFactory vocaGroupFactory;

    public VocaListRes findGroupedVoca(final Long userId, final int sort) {
        List<Voca> vocas = (sort == 1)
                ? vocaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                : vocaRepository.findAllByUserIdOrderByPhraseAsc(userId);

        return (sort == 1)
                ? vocaGroupFactory.createByDateGroup(vocas)
                : vocaGroupFactory.createByFirstLetter(vocas);
    }

    // TODO : 검색 한글 예외처리 추가 (단어장 사용 시)
    public List<Voca> findStartsWithVoca(final Long userId, final String keyword) {
        return vocaRepository.findAllByUserIdAndPhraseStartsWith(userId, keyword);
    }

    public boolean existsByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRepository.existsByUserIdAndRecommendId(userId, recommendId);
    }

    public Optional<Voca> findOptionalByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRepository.findByUserIdAndRecommendId(userId, recommendId);
    }
}
