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

    boolean existsByUserAndWrittenDate(User user, LocalDate writtenDate);

    @Query("""
    SELECT d FROM Diary d
    WHERE d.user.id = :userId
      AND d.createdAt >= :startOfDay
      AND d.createdAt < :endOfDay
""")
    List<Diary> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
    SELECT d.createdAt FROM Diary d
    WHERE d.user.id = :userId
""")
    List<LocalDateTime> findCreatedAtsByUserId(@Param("userId") Long userId);

    Optional<Diary> findFirstByUserIdAndWrittenDate(Long userId, LocalDate writtenDate);

}
