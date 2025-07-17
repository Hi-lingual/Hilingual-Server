package org.hilingual.domain.recommend.core.repository;

import org.hilingual.domain.recommend.core.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select r from Recommend r where r.id in (select v.recommend.id from Voca v where v.id = :vocaId)")
    List<Recommend> findAllByVocaId(@Param("vocaId") Long vocaId);

    List<Recommend> findByDiaryId(Long diaryId);

    @Query("""
    SELECT r FROM Recommend r
    JOIN FETCH r.diary d
    WHERE r.id = :phraseId AND d.user.id = :userId
""")
    Optional<Recommend> findPhraseByIdAndUserId(@Param("phraseId") Long phraseId,
                                                @Param("userId") Long userId);
}
