package org.sopt.userprofile.repository;

import org.sopt.user.domain.User;
import org.sopt.userprofile.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNickname(String nickname);

    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u.id = :userId
    """)
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);

    Optional<UserProfile> findByUser(User user);

    @Query("""
           SELECT up
           FROM UserProfile up
           WHERE up.user.id IN :userIds
           """)
    List<UserProfile> findByUserIds(@Param("userIds") List<Long> userIds);
}

