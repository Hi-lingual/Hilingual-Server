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

import static org.sopt.notice.domain.NoticeTableConstants.*;

@Entity
@Table(name = TABLE_NOTICE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Convert(converter = CategoryConverter.class)
    @Column(name = COLUMN_CATEGORY, nullable = false)
    private Category category;

    @Column(name = COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = COLUMN_IS_ACTIVE, nullable = false)
    private Boolean isActive; // 공지 내릴 때 사용

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = COLUMN_NOTICE_DETAIL_ID, nullable = false)
    private NoticeDetail noticeDetail;

    public static Notice create(Category category, String title, String content) {
        return new Notice(
                null,
                category,
                title,
                true,
                NoticeDetail.create(content)
        );
    }
}
