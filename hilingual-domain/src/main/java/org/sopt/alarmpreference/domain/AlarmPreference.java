package org.sopt.alarmpreference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.alarmpreference.domain.type.AlarmType;
import org.sopt.alarmpreference.domain.type.AlarmTypeConverter;
import org.sopt.user.domain.User;

@Entity
@Table(name = AlarmPreferenceTableConstants.TABLE_ALARM_PREFERENCE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlarmPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmPreferenceTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = AlarmPreferenceTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Convert(converter = AlarmTypeConverter.class)
    @Column(name = AlarmPreferenceTableConstants.COLUMN_ALARM_TYPE, nullable = false)
    private AlarmType alarmType;

    @Column(name = AlarmPreferenceTableConstants.COLUMN_IS_ENABLED, nullable = false)
    private Boolean isEnabled;

}
