package org.sopt.forbiddenword.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.forbiddenword.repository.ForbiddenWordRepository;

import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ForbiddenWordRetriever {

    private final ForbiddenWordRepository forbiddenWordRepository;

    public boolean findIsInForbiddenWord(final String nickname) {
        return forbiddenWordRepository.existsByForbiddenWordInNickname(nickname);
    }

}
