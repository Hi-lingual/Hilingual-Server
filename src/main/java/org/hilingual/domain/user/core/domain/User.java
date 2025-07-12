package org.hilingual.domain.user.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.usesrprofile.UserProfile;

import java.time.LocalDateTime;

import static org.hilingual.domain.user.core.domain.UserTableConstants.*;

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

    @Column(name = COLUMN_PROVIDER, nullable = false, length = 20)
    private String provider; // GOOGLE | APPLE

    @Column(name = COLUMN_PROVIDER_ID, nullable = false, length = 255)
    private String providerId; // 유저 고유 ID

    @Column(name = COLUMN_IS_COMPLETED, nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isCompleted = false;

    @Column(name = COLUMN_IS_DELETED, nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = COLUMN_DELETED_AT)
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

}
