package org.sopt.user.facade;

import lombok.RequiredArgsConstructor;

import org.sopt.user.domain.User;
import org.sopt.user.exception.UserCoreErrorCode;
import org.sopt.user.exception.UserNotFoundException;
import org.sopt.user.repository.UserRepository;
import org.sopt.userprofile.repository.UserProfileRepository;
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

    public User findByUserIdWithLock(final long userId) {
        return userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new UserNotFoundException(UserCoreErrorCode.USER_NOT_FOUND));
    }

    public boolean isNicknameExists(String nickname) {
        return userProfileRepository.existsByNickname(nickname);
    }
  
}
