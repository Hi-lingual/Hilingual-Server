package org.hilingual.domain.recommend.core.repository;

import org.hilingual.domain.recommend.core.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select r from Recommend r where r.id in (select v.recommend.id from Voca v where v.id = :vocaId)")
    List<Recommend> findAllByVocaId(@Param("vocaId") Long vocaId);
}
