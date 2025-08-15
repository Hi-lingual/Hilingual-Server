package org.sopt.noticedetail.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = NoticeDetailTableConstants.TABLE_NOTICE_DETAIL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NoticeDetailTableConstants.COLUMN_ID)
    private Long id;

    @Column(name = NoticeDetailTableConstants.COLUMN_CONTENT, nullable = false, columnDefinition = "TEXT")
    private String content;
}
