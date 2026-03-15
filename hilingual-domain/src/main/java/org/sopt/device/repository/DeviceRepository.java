package org.sopt.device.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.sopt.device.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByUser_IdAndUuid(Long userId, String uuid);

    Optional<Device> findByUser_IdAndDeviceName(Long userId, String deviceName);

    @Modifying(clearAutomatically = true)
    @Query("delete from Device d where d.user.id = :userId")
    void deleteAllByUser_Id(@Param("userId") Long userId);
}
