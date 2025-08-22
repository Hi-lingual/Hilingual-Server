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

    // 유저ID에 따른 LikedDiary 조회(Diary, User, UserProfile 함께 조회) 및 최신순 정렬
    @Query("""
            SELECT ld FROM LikedDiary ld
            JOIN FETCH ld.diary d
            JOIN FETCH d.user u
            JOIN FETCH u.userProfile up
            WHERE ld.user.id = :userId
            ORDER BY ld.createdAt DESC
            """)
    List<LikedDiary> findLikedDiariesWithDetailsByUserId(@Param("userId") Long userId);
}
