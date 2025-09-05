package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.domain.VocaTableConstants;
import org.sopt.voca.exception.VocaCoreErrorCode;
import org.sopt.voca.exception.VocaLimitExceededException;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class VocaSaver {

    private final VocaRepository vocaRepository;

    private static final int MAX_VOCA_PER_USER = 1000;

    @Transactional
    public void saveIfNotExists(final User user, final Recommend recommend) {
        final Long userId = user.getId();
        final Long recommendId = recommend.getId();

        if (vocaRepository.existsByUserIdAndRecommendId(userId, recommendId)) return;

        long current = vocaRepository.countByUserId(userId);
        if (current >= MAX_VOCA_PER_USER) {
            throw new VocaLimitExceededException(VocaCoreErrorCode.VOCA_LIMIT_EXCEEDED);
        }

        final boolean mine = recommend.getDiary().getUser().getId().equals(user.getId());
        final String writtenFrom = mine
                ? recommend.getDiary().getWrittenDate().format(DateTimeFormatter.ofPattern("yy.MM.dd")) + " 일기에서 저장됨"
                : "피드에서 저장됨";

        final Voca voca = mine
                ? Voca.fromMyDiary(user, recommend, writtenFrom)
                : Voca.fromFeed(user, recommend, writtenFrom);

        try {
            vocaRepository.save(voca);
        } catch (DataIntegrityViolationException e) {
            if (isUniqueViolation(e, VocaTableConstants.UK_VOCA_USER_RECOMMEND)) return;
            throw e;
        }
    }

    private boolean isUniqueViolation(DataIntegrityViolationException e, String constraintName) {
        Throwable cause = e.getMostSpecificCause();
        String msg = (cause != null ? cause.getMessage() : null);
        return msg != null && msg.contains(constraintName);
    }
}