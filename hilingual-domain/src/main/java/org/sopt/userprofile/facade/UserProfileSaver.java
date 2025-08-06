package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileSaver {

    private final UserProfileRepository userProfileRepository;

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
    public List<UserProfile> saveAll(List<UserProfile> profiles) {
        return userProfileRepository.saveAll(profiles);
    }
}
