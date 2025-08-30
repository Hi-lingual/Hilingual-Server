package org.sopt.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.sopt.user.type.RegisterStatus;
import org.sopt.user.type.RegisterStatusConverter;
import org.sopt.userprofile.domain.UserProfile;

import java.time.LocalDateTime;
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

    @Convert(converter = RegisterStatusConverter.class)
    @Column(name = COLUMN_REGISTER_STATUS, nullable = false)
    private RegisterStatus registerStatus;

    @Column(name = COLUMN_IS_COMPLETED, nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isCompleted = false; // TODO 소셜로그인 붙이며 registerStatus 활용하는 로직으로 변경

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
}
