package org.sopt.feed.repository;

import org.sopt.diary.domain.Diary;
import org.sopt.feed.dto.FeedProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Diary, Long> {

    /*
     * 추천 피드 조회
     * - 내가 공유한 일기를 포함해 모든 유저의 공개된 일기
     * - 총 50개, SharedTime 기준 최신순 정렬
     * - 차단한/차단당한 유저는 보이지 않음
     */
    @Query("""
        SELECT DISTINCT
            d.id AS diaryId,
            d.sharedTime AS sharedDate,
            d.isLiked AS likeCount,
            d.imageUrl AS diaryImg,
            d.originalText AS originalText,
            d.user.id AS userId,
            (CASE WHEN d.user.id = :currentUserId THEN true ELSE false END) AS isMine,
            d.user.userProfile.profileImg AS profileImg,
            d.user.userProfile.nickname AS nickname,
            d.user.userProfile.streak AS streak,
            (CASE WHEN EXISTS (
                SELECT 1 FROM LikedDiary l WHERE l.user.id = :currentUserId AND l.diary.id = d.id
            ) THEN true ELSE false END) AS isLiked
        FROM Diary d
        WHERE d.isPublic = true
          AND NOT EXISTS (
              SELECT 1 FROM Block b WHERE b.blocker.id = :currentUserId AND b.blocked.id = d.user.id
          )
          AND NOT EXISTS (
              SELECT 1 FROM Block b WHERE b.blocker.id = d.user.id AND b.blocked.id = :currentUserId
          )
        ORDER BY d.sharedTime DESC
    """)
    List<FeedProjection> findRecommendFeeds(
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );

    /*
     * 팔로잉 피드 조회
     * - 내가 팔로우하는 유저의 공개된 일기
     * - 총 50개, SharedTime 기준 최신순 정렬
     * - 차단한/차단당한 유저는 보이지 않음
     */
    @Query("""
        SELECT DISTINCT
            d.id AS diaryId,
            d.sharedTime AS sharedDate,
            d.isLiked AS likeCount,
            d.imageUrl AS diaryImg,
            d.originalText AS originalText,
            d.user.id AS userId,
            (CASE WHEN d.user.id = :currentUserId THEN true ELSE false END) AS isMine,
            d.user.userProfile.profileImg AS profileImg,
            d.user.userProfile.nickname AS nickname,
            d.user.userProfile.streak AS streak,
            (CASE WHEN EXISTS (
                SELECT 1 FROM LikedDiary l WHERE l.user.id = :currentUserId AND l.diary.id = d.id
            ) THEN true ELSE false END) AS isLiked
        FROM Diary d
        WHERE d.isPublic = true
          AND EXISTS (
              SELECT 1 FROM Follow f WHERE f.follower.id = :currentUserId AND f.followee.id = d.user.id
          )
          AND NOT EXISTS (
              SELECT 1 FROM Block b WHERE b.blocker.id = :currentUserId AND b.blocked.id = d.user.id
          )
          AND NOT EXISTS (
              SELECT 1 FROM Block b WHERE b.blocker.id = d.user.id AND b.blocked.id = :currentUserId
          )
        ORDER BY d.sharedTime DESC
    """)
    List<FeedProjection> findFollowingFeeds(
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );
}
