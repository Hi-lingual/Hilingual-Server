package org.sopt.device.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.device.domain.type.DeviceType;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;

import static org.sopt.device.domain.DeviceTableConstants.*;

@Entity
@Table(
        name = TABLE_DEVICE,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_USER_ID_UUID,
                        columnNames = {
                                COLUMN_USER_ID,
                                COLUMN_UUID
                        }
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_DEVICE_ID)
    private  Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @Column(name = COLUMN_UUID)
    private String uuid;

    @Column(name = COLUMN_TIMEZONE)
    private String timezone;

    @Column(name = COLUMN_DEVICE_NAME)
    private String deviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_DEVICE_TYPE)
    private DeviceType deviceType;

    @Column(name = COLUMN_OS_TYPE)
    private String osType;

    @Column(name = COLUMN_OS_VERSION)
    private String osVersion;

    @Column(name = COLUMN_APP_VERSION)
    private String appVersion;

    @Column(name = COLUMN_FCM_TOKEN)
    private String fcmToken;

    @Column(name = COLUMN_FCM_TOKEN_UPDATED_AT)
    private LocalDateTime fcmTokenUpdatedAt;

    // 최초 생성
    public static Device create(
            User user, String uuid, String timezone,
            String deviceName, DeviceType deviceType,
            String osType, String osVersion, String appVersion
    ) {
        return new Device(
                null, user, uuid, timezone,
                deviceName, deviceType, osType, osVersion, appVersion,
                null, null
        );
    }

    // 기존 기기 정보 갱신
    public void updateDeviceInfo(
            String timezone, String osVersion, String appVersion
    ) {
        this.timezone = timezone;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        this.fcmTokenUpdatedAt = LocalDateTime.now();
    }

    public void clearFcmToken() {
        this.fcmToken = null;
        this.fcmTokenUpdatedAt = LocalDateTime.now();
    }
}