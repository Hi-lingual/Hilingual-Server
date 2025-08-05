package org.sopt.recommend.repository;

import org.sopt.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    List<Recommend> findByDiaryId(Long diaryId);

    @Query("""
    SELECT r FROM Recommend r
    JOIN FETCH r.diary d
    WHERE r.id = :phraseId AND d.user.id = :userId
""")
    Optional<Recommend> findPhraseByIdAndUserId(@Param("phraseId") Long phraseId,
                                                @Param("userId") Long userId);
}
