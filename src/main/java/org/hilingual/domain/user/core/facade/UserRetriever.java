package org.hilingual.domain.user.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.usesrprofile.core.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {
    private final UserProfileRepository userProfileRepository;

    public boolean isNicknameExists(String nickname) {
        return userProfileRepository.existsByNickname(nickname);
    }
}
