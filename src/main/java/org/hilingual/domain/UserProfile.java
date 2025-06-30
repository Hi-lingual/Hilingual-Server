package org.hilingual.domain;

import jakarta.persistence.*;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false, length = 512)
    private String profileImg;

    @Column(nullable = false)
    private Integer totalDiaries = 0;

    @Column(nullable = false)
    private Integer streak = 0;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
