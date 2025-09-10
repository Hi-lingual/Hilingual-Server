package org.sopt.diary.repository;

import org.sopt.diary.domain.Diary;
import org.sopt.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /** 같은 날짜 재작성 금지*/
    boolean existsByUserAndWrittenDate(User user, LocalDate writtenDate);

    @Query("""
    SELECT d.createdAt FROM Diary d
    WHERE d.user.id = :userId
""")
    List<LocalDateTime> findCreatedAtsByUserId(@Param("userId") Long userId);

    /** 본인 소유의 일기 단건 조회*/
    Optional<Diary> findFirstByUserIdAndWrittenDate(Long userId, LocalDate writtenDate);

    Optional<Diary> findByIdAndUserId(Long diaryId, Long userId);

    List<Diary> findByUserIdAndIsPublicTrueOrderBySharedTimeDesc(Long userId);

    // ID를 통해 다이어리 조회(+User, UserProfile 함께 조회)
    @Query("""
           SELECT d
           FROM Diary d
           JOIN FETCH d.user u
           JOIN FETCH u.userProfile up
           WHERE d.id = :diaryId AND d.isPublic = TRUE
           """)
    Optional<Diary> findDiaryWithDetailsById(@Param("diaryId") Long diaryId);

    void deleteAllByUserId(Long userId);
}
