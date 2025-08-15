package org.sopt.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = AlarmTableConstants.TABLE_NOTICE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmTableConstants.COLUMN_ID)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = AlarmTableConstants.COLUMN_CATEGORY, nullable = false, length = 20)
    private Category category;

    @Column(name = AlarmTableConstants.COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = AlarmTableConstants.COLUMN_IS_ACTIVE, nullable = false)
    private Boolean isActive;

    @Column(name = AlarmTableConstants.COLUMN_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = AlarmTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;

    @OneToOne(mappedBy = AlarmTableConstants.COLUMN_NOTICE, cascade = CascadeType.ALL)
    private NoticeDetail noticeDetails;

    public enum Category {
        NOTIFICATION,
        MARKETING
    }
}
