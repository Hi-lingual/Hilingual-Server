package org.sopt.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRetriever userRetriever;
    private final UserSaver userSaver;

    public User getUserById(final long userId) {
        return userRetriever.findByUserId(userId);
    }

    public User getUserByIdWithLock(final long userId){
        return userRetriever.findByUserIdWithLock(userId);
    }

    public void save(final User user){
        userSaver.save(user);
    }
}