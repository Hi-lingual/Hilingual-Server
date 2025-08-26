package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.domain.VocaTableConstants;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class VocaSaver {

    private final VocaRepository vocaRepository;

    @Transactional
    public void saveIfNotExists(final User user, final Recommend recommend) {
        if (vocaRepository.existsByUserIdAndRecommendId(user.getId(), recommend.getId())) return;

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