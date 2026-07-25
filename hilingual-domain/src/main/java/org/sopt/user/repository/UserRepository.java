package org.sopt.user.repository;


import jakarta.persistence.LockModeType;
import org.sopt.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    List<User> findByExternalIdIn(List<String> externalIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithLock(@Param("id") Long userId);

    boolean existsByProviderId(String providerId);

    // 지정된 유저 id 목록에 대해 notifyStatus를 true로 변경
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update User u
              set u.notifyStatus = true
            where u.id in :ids
              and u.notifyStatus = false
           """)
    void turnOnNotifyByIds(@Param("ids") List<Long> ids);

    // 탈퇴/삭제된 계정 제외 전체 유저 id
    @Query("select u.id from User u where u.isDeleted = false")
    List<Long> findAllActiveUserIds();
}
