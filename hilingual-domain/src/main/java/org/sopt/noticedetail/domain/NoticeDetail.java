package org.sopt.noticedetail.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.feedalarm.domain.AlarmTableConstants;
import org.sopt.notice.domain.Notice;

@Entity
@Table(name = AlarmTableConstants.TABLE_NOTICE_DETAIL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmTableConstants.COLUMN_ID)
    private Long id;

    @OneToOne
    @JoinColumn(name = AlarmTableConstants.COLUMN_NOTICE_ID, nullable = false)
    private Notice notice;

    @Column(name = AlarmTableConstants.COLUMN_CONTENT, nullable = false, columnDefinition = "TEXT")
    private String content;
}
