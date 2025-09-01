package org.sopt.alarmpreference.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.alarmpreference.type.AlarmType;
import org.sopt.alarmpreference.type.AlarmTypeConverter;
import org.sopt.user.domain.User;

import static org.sopt.alarmpreference.domain.AlarmPreferenceTableConstants.*;

@Entity
@Table(
        name = TABLE_ALARM_PREFERENCE,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_ALARM_TYPE_USER_ID,
                        columnNames = {
                                COLUMN_ALARM_TYPE,
                                COLUMN_USER_ID
                        }
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlarmPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @Convert(converter = AlarmTypeConverter.class)
    @Column(name = COLUMN_ALARM_TYPE, nullable = false)
    private AlarmType alarmType;

    @Column(name = COLUMN_IS_ENABLED, nullable = false)
    private Boolean isEnabled;

    public static AlarmPreference create(User user, AlarmType alarmType, Boolean isEnabled) {
        return new AlarmPreference(
                null,
                user,
                alarmType,
                isEnabled
        );
    }

    public void toggle() {
        this.isEnabled = !Boolean.TRUE.equals(this.isEnabled);
    }

}
