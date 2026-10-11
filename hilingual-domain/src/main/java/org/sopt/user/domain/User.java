package org.sopt.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.context.TimezoneContextHolder;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.sopt.type.UserRole;
import org.sopt.user.type.RegisterStatus;
import org.sopt.user.type.RegisterStatusConverter;
import org.sopt.userprofile.domain.UserProfile;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.sopt.user.domain.UserTableConstants.*;

@Entity
@Table(
        name = TABLE_USER,
        uniqueConstraints = @UniqueConstraint(
                columnNames = {COLUMN_PROVIDER, COLUMN_PROVIDER_ID}
        )
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_ROLE)
    private UserRole role;

    @Column(name = COLUMN_PROVIDER, nullable = false, length = 20)
    private String provider; // GOOGLE | APPLE

    @Column(name = COLUMN_PROVIDER_ID, nullable = false, length = 255)
    private String providerId; // 유저 고유 ID

    @Column(name = COLUMN_EXTERNAL_ID, nullable = false, length = 64, updatable = false)
    private String externalId; // provider+providerId 의 SHA-256 hex. 재가입해도 동일.

    @Convert(converter = RegisterStatusConverter.class)
    @Column(name = COLUMN_REGISTER_STATUS, nullable = false)
    private RegisterStatus registerStatus = RegisterStatus.SOCIAL_LOGIN_COMPLETED;

    @Column(name = COLUMN_PRIMARY_TIMEZONE, nullable = false)
    private String primaryTimezone = "Asia/Seoul";

    @Column(name = COLUMN_IS_DELETED, nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = COLUMN_DELETED_AT)
    private LocalDateTime deletedAt;

    // User - UserProfile 양방향 매핑
    @OneToOne(mappedBy = COLUMN_USER, cascade = CascadeType.ALL)
    private UserProfile userProfile;

    // User - NoticeDelivery 양방향 매핑
    @OneToMany(mappedBy = COLUMN_USER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeDelivery> deliveries = new ArrayList<>();

    @Column(name = UserTableConstants.COLUMN_NOTIFY_STATUS, nullable = false)
    private Boolean notifyStatus = false;

    public void updateRegisterStatus(final RegisterStatus registerStatus) {
        this.registerStatus = registerStatus;
    }

    public ZoneId getZoneIdOrDefault() {
        if (primaryTimezone == null) return TimezoneContextHolder.DEFAULT_ZONE;
        try {
            return ZoneId.of(primaryTimezone);
        } catch (DateTimeException e) {
            return TimezoneContextHolder.DEFAULT_ZONE;
        }
    }

    // TODO 추후 엔티티에서 userFacade.updateTimezone으로 변경하고 함수 호출 해당 함수로 수정
    public void updatePrimaryTimezone(ZoneId zone) {   // String 오버로드가 있다면 이 메서드로 위임
        if (TimezoneContextHolder.find().isEmpty()) return;
        if (!ZoneId.getAvailableZoneIds().contains(zone.getId())) return;
        this.primaryTimezone = zone.getId();
    }

    public void revertDeleteUser() {
        this.isDeleted = false;
    }

    public static User ref(Long id) {
        User u = new User();
        u.id = id;
        return u;
    }

    public void turnOnNotify() {
        this.notifyStatus = true;
    }

    public void turnOffNotify() {
        this.notifyStatus = false;
    }
}
