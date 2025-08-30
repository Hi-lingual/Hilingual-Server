package org.sopt.forbiddenword.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ForbiddenWordFacade {

    private final ForbiddenWordRetriever forbiddenWordRetriever;

    public boolean findIsInForbiddenWord(String nickname) {
        return forbiddenWordRetriever.findIsInForbiddenWord(nickname);
    }

}
