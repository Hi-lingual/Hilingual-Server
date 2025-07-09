package org.hilingual.domain.diary.core.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.user.core.domain.User;

import java.util.List;

import static org.hilingual.domain.diary.core.domain.DiaryTableConstants.*;

@Entity
@Table(name = TABLE_DIARY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_ORIGINAL_TEXT, nullable = false, length = 1000)
    private String originalText;

    @Column(name = COLUMN_REWRITE_TEXT, nullable = false, length = 1500)
    private String rewriteText;

    @Column(name = COLUMN_IMAGE_URL)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DiaryFeedback> feedbacks;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommend> recommends;
}