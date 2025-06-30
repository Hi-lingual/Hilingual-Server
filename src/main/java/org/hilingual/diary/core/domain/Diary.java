package org.hilingual.diary.core.domain;

import jakarta.persistence.*;
import org.hilingual.user.core.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @Column(nullable = false)
    private String originalText;

    @Column(nullable = false)
    private String rewriteText;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DiaryFeedback> feedbacks;

    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommend> recommends;
}
