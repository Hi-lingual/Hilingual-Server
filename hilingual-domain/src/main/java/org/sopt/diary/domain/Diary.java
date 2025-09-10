package org.sopt.diary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.likeddiary.domain.LikedDiary;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.sopt.diary.domain.DiaryTableConstants.*;

@Entity
@Table(name = DiaryTableConstants.TABLE_DIARY)
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

    @Column(name = COLUMN_WRITTEN_DATE)
    private LocalDate writtenDate;

    @Column(name = COLUMN_IS_PUBLIC, nullable = false)
    private Boolean isPublic = Boolean.FALSE;

    @Column(name = COLUMN_IS_LIKED, nullable = false)
    private Integer isLiked = 0;

    @Column(name = COLUMN_SHARED_TIME)
    private LocalDateTime sharedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryFeedback> feedbacks;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommends;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LikedDiary> likes = new ArrayList<>();

    public void publish() {
        this.isPublic = true;
        this.sharedTime = LocalDateTime.now();
    }

    public void unpublish() {
        this.isPublic = false;
        this.sharedTime = null;
        this.isLiked = 0;
    }

    public static Diary create(
            User user, String originalText, String rewriteText,
            String imageUrl, LocalDate writtenDate
    ) {
        return new Diary(
                null,
                originalText,
                rewriteText,
                imageUrl,
                writtenDate,
                Boolean.FALSE, // isPublic
                0,             // isLiked
                null,
                user,
                null,
                null,
                null
        );
    }

    public void increaseLikeCount() {
        this.isLiked = (this.isLiked == null ? 0 : this.isLiked) + 1;
    }
    public void decreaseLikeCount() {
        int cur = (this.isLiked == null ? 0 : this.isLiked);
        this.isLiked = Math.max(0, cur - 1);
    }

}
