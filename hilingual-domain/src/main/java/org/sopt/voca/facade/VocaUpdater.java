package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.exception.VocaCoreErrorCode;
import org.sopt.voca.exception.VocaInvalidMemorizationTargetException;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VocaUpdater{

    private final VocaRepository vocaRepository;

    @Transactional
    public void updateMemorization(final Long userId, final Map<Long, Boolean> memorizationByRecommendId){
        final Set<Long> recommendIds = memorizationByRecommendId.keySet();
        final List<Voca> vocas = vocaRepository.findAllByUserIdAndRecommendIdIn(userId, recommendIds);

        //All-or-nothing - 요청한 단어가 전부 본인 소유로 조회되어야 함
        if(vocas.size()!=recommendIds.size()){
            throw new VocaInvalidMemorizationTargetException(VocaCoreErrorCode.INVALID_MEMORIZATION_TARGET);
        }

        vocas.forEach(voca ->voca.updateMemorized(memorizationByRecommendId.get(voca.getRecommendId())));
    }

}