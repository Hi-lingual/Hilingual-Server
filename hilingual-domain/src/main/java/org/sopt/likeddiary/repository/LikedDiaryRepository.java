package org.sopt.likeddiary.repository;

import org.sopt.likeddiary.domain.LikedDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LikedDiaryRepository extends JpaRepository<LikedDiary, Long> {

    @Query("SELECT ld.diary.id FROM LikedDiary ld WHERE ld.user.id = :userId AND ld.diary.id IN :diaryIds")
    List<Long> findLikedDiaryIdsByUserIdAndDiaryIdsIn(
            @Param("userId") Long userId,
            @Param("diaryIds") List<Long> diaryIds
    );
}
