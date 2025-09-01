package org.sopt.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSaver {

    private final UserRepository userRepository;

    public User save(final User user){
        return userRepository.save(user);
    }
}
