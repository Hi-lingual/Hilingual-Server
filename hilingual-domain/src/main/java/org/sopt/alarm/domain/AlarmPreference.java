package org.sopt.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

@Entity
@Table(name = AlarmTableConstants.TABLE_ALRAM_PREFERENCE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlarmPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = AlarmTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = AlarmTableConstants.COLUMN_ALARM_TYPE, nullable = false, length = 20)
    private AlarmType alarmType;

    @Column(name = AlarmTableConstants.COLUMN_IS_ENABLED, nullable = false)
    private Boolean isEnabled;

    public enum AlarmType {
        FEED,
        MARKETING
    }
}