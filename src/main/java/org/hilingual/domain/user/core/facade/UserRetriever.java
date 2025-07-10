package org.hilingual.domain.user.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.exception.UserCoreErrorCode;
import org.hilingual.domain.user.core.exception.UserNotFoundException;
import org.hilingual.domain.user.core.repository.UserRepository;
import org.hilingual.domain.usesrprofile.core.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public User findByUserId(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserCoreErrorCode.USER_NOT_FOUND));
    }

    public boolean isNicknameExists(String nickname) {
        return userProfileRepository.existsByNickname(nickname);
    }
  
}