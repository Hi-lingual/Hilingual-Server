package org.sopt.voca.repository;

import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.domain.Voca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VocaRepository extends JpaRepository<Voca, Long> {

    void deleteByUserAndRecommend(User user, Recommend recommend);

    boolean existsByUserAndRecommend(User user, Recommend recommend);


    // AZ순 정렬 (Recommend.phrase 기반)
    @Query("""
        SELECT v FROM Voca v
        JOIN FETCH v.recommend r
        WHERE v.user.id = :userId
        ORDER BY LOWER(r.phrase)
    """)
    List<Voca> findAllByUserIdOrderByPhraseAsc(@Param("userId") Long userId);

    // 최신순 정렬 (Voca.createdAt 기반)
    @Query("""
        SELECT v FROM Voca v
        JOIN FETCH v.recommend r
        WHERE v.user.id = :userId
        ORDER BY v.createdAt DESC
    """)
    List<Voca> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("""
    SELECT v FROM Voca v
    JOIN FETCH v.recommend r
    WHERE v.user.id = :userId
      AND LOWER(r.phrase) LIKE LOWER(CONCAT(:keyword, '%'))
    ORDER BY LOWER(SUBSTRING(r.phrase, LENGTH(:keyword) + 1))
""")
    List<Voca> findAllByUserIdAndPhraseStartsWith(
            @Param("userId") Long userId,
            @Param("keyword") String keyword
    );

    // 특정 단어 상세 조회
    @Query("""
        SELECT v
        FROM Voca v
        JOIN FETCH v.recommend r
        JOIN FETCH r.diary d
        WHERE v.user.id = :userId AND r.id = :phraseId
    """)
    Optional<Voca> findDetailByUserIdAndPhraseId(@Param("userId") Long userId,
                                                 @Param("phraseId") Long phraseId);

}

