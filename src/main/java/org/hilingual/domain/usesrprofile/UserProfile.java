package org.hilingual.domain.usesrprofile;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.user.core.domain.User;

import static org.hilingual.domain.usesrprofile.UserProfileTableConstants.*;

@Entity
@Table(name = TABLE_USER_PROFILE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_NICKNAME, nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = COLUMN_PROFILE_IMG, nullable = false, length = 512)
    private String profileImg;

    @Column(name = COLUMN_TOTAL_DIARIES, nullable = false)
    private Integer totalDiaries = 0;

    @Column(name = COLUMN_STREAK, nullable = false)
    private Integer streak = 0;

    @OneToOne
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;
}