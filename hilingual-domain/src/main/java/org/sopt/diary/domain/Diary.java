package org.sopt.diary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = DiaryTableConstants.TABLE_DIARY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DiaryTableConstants.COLUMN_ID)
    private Long id;

    @Column(name = DiaryTableConstants.COLUMN_ORIGINAL_TEXT, nullable = false, length = 1000)
    private String originalText;

    @Column(name = DiaryTableConstants.COLUMN_REWRITE_TEXT, nullable = false, length = 1500)
    private String rewriteText;

    @Column(name = DiaryTableConstants.COLUMN_IMAGE_URL)
    private String imageUrl;

    @Column(name = DiaryTableConstants.COLUMN_WRITTEN_DATE)
    private LocalDate writtenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DiaryTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DiaryFeedback> feedbacks;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommend> recommends;

    public static Diary create(User user, String originalText, String rewriteText, String imageUrl, LocalDate writtenDate) {
        return new Diary(null, originalText, rewriteText, imageUrl, writtenDate, user, null, null);
    }

}
