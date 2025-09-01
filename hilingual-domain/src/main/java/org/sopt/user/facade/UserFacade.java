package org.sopt.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRetriever userRetriever;
    private final UserSaver userSaver;

    public Optional<User> getByProviderAndProviderId(final String provider, final String providerId) {
        return userRetriever.findByProviderAndProviderId(provider, providerId);
    }

    public User getUserById(final long userId) {
        return userRetriever.findByUserId(userId);
    }

    public User getUserByIdWithLock(final long userId){
        return userRetriever.findByUserIdWithLock(userId);
    }

    public User save(final User user){
        return userSaver.save(user);
    }
}