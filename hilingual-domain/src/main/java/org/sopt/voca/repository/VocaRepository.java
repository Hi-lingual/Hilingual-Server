package org.sopt.voca.repository;

import org.sopt.voca.domain.Voca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VocaRepository extends JpaRepository<Voca, Long> {

    // 존재 여부 (user.id + recommendId)
    boolean existsByUserIdAndRecommendId(Long userId, Long recommendId);

    // 유저별 voca 저장 갯수
    long countByUserId(Long userId);

    // 북마크 해제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from Voca v
         where v.user.id = :userId
           and v.recommendId = :recommendId
    """)
    int deleteByUserIdAndRecommendId(@Param("userId") long userId,
                                     @Param("recommendId") long recommendId);

    // A~Z 정렬
    @Query("""
        SELECT v FROM Voca v
        WHERE v.user.id = :userId
        ORDER BY LOWER(v.phrase)
    """)
    List<Voca> findAllByUserIdOrderByPhraseAsc(@Param("userId") Long userId);

    // 최신순 정렬 (Voca.createdAt 기반)
    @Query("""
        SELECT v FROM Voca v
        WHERE v.user.id = :userId
        ORDER BY v.createdAt DESC
    """)
    List<Voca> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    // 검색 (Prefix 기반)
    @Query("""
        SELECT v FROM Voca v
        WHERE v.user.id = :userId
          AND LOWER(v.phrase) LIKE LOWER(CONCAT(:keyword, '%'))
        ORDER BY LOWER(SUBSTRING(v.phrase, LENGTH(:keyword) + 1))
    """)
    List<Voca> findAllByUserIdAndPhraseStartsWith(@Param("userId") Long userId,
                                                  @Param("keyword") String keyword);

    // Voca 상세
    @Query("""
        SELECT v FROM Voca v
        WHERE v.user.id = :userId AND v.recommendId = :phraseId
    """)
    Optional<Voca> findDetailByUserIdAndPhraseId(@Param("userId") Long userId,
                                                 @Param("phraseId") Long phraseId);
}