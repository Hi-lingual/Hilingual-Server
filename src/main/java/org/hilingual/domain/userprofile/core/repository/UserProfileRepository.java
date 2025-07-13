package org.hilingual.domain.userprofile.core.repository;

import org.hilingual.domain.userprofile.core.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNickname(String nickname);

    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u.id = :userId
    """)
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);
}

