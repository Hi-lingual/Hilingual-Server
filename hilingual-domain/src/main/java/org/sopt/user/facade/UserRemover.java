package org.sopt.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.exception.UserCoreErrorCode;
import org.sopt.user.exception.UserNotFoundException;
import org.sopt.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRemover {
    private final UserRepository userRepository;

    @Transactional
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(UserCoreErrorCode.USER_NOT_FOUND);
        }

        userRepository.deleteById(userId);
    }
}
