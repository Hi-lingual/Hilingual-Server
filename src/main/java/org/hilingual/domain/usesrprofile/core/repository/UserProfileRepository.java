package org.hilingual.domain.usesrprofile.core.repository;

import org.hilingual.domain.usesrprofile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNickname(String nickname);
}
