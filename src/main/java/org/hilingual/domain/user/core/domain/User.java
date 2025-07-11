package org.hilingual.domain.user.core.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_PROVIDER, nullable = false, length = 20)
    private String provider;

    @Column(name = COLUMN_PROVIDER_ID, nullable = false, length = 255)
    private String providerId;

    @Column(name = COLUMN_IS_COMPLETED, nullable = false)
    private Boolean isCompleted = false;

    @Column(name = COLUMN_IS_DELETED, nullable = false)
    private Boolean isDeleted = false;

    @Column(name = COLUMN_DELETED_AT)
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;
}
