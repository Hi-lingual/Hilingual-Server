package org.hilingual.domain.user.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.facade.UserRetriever;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRetriever userRetriever;

    public User findById(final long userId) {
        return userRetriever.findByUserId(userId);
    }
}