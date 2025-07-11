package org.hilingual.domain.voca.core.repository;

import org.hilingual.domain.voca.core.domain.Voca;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VocaRepository extends Repository<Voca, Long> {

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

    @Query("""
    SELECT v FROM Voca v
    JOIN FETCH v.recommend r
    WHERE v.id = :vocaId
      AND v.user.id = :userId
""")
    Optional<Voca> findByIdAndUserId(@Param("vocaId") Long vocaId, @Param("userId") Long userId);
}

