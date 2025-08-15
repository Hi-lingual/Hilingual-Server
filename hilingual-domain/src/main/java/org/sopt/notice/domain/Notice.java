package org.sopt.notice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.notice.type.Category;
import org.sopt.notice.type.CategoryConverter;
import org.sopt.noticedetail.domain.NoticeDetail;
import java.time.LocalDateTime;

@Entity
@Table(name = NoticeTableConstants.TABLE_NOTICE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NoticeTableConstants.COLUMN_ID)
    private Long id;

    @Convert(converter = CategoryConverter.class)
    @Column(name = NoticeTableConstants.COLUMN_CATEGORY, nullable = false)
    private Category category;

    @Column(name = NoticeTableConstants.COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = NoticeTableConstants.COLUMN_IS_ACTIVE, nullable = false)
    private Boolean isActive;

    @Column(name = NoticeTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;

    @OneToOne(mappedBy = NoticeTableConstants.COLUMN_NOTICE, cascade = CascadeType.ALL)
    private NoticeDetail noticeDetails;
}
