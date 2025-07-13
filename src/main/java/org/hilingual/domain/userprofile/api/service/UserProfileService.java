package org.hilingual.domain.userprofile.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.core.facade.UserProfileRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserProfileRetriever userProfileRetriever;

    public UserProfileResponse getUserProfile(final Long userId) {
        return userProfileRetriever.getUserProfile(userId);
    }
}
