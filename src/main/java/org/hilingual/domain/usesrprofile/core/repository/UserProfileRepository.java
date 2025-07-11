package org.hilingual.domain.usesrprofile.core.repository;

import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.usesrprofile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNickname(String nickname);
    Optional<UserProfile> findByUser(User user);
}
