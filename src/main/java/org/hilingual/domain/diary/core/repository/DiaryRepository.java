package org.hilingual.domain.diary.core.repository;

import org.hilingual.domain.diary.core.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
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

}
