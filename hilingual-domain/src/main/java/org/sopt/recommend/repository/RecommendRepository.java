package org.sopt.recommend.repository;

import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.dto.RecommendWithBookmarkDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("""
    select new org.sopt.recommend.dto.RecommendWithBookmarkDto(
        r.id,
        r.phraseType,
        r.phrase,
        r.explanation,
        r.reason,
        case when (v.id is not null) then true else false end
    )
    from Recommend r
    left join Voca v on v.recommendId = r.id and v.user.id = :userId
    where r.diary.id = :diaryId
""")
    List<RecommendWithBookmarkDto> findWithBookmarkFlag(
            @Param("userId") Long userId,
            @Param("diaryId") Long diaryId
    );

    @Query("""
        select r from Recommend r
        join fetch r.diary d
        where r.id = :id
    """)
    Optional<Recommend> findByIdWithDiary(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Recommend r where r.diary.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
