package org.hilingual.domain.recommend.core.repository;

import org.hilingual.domain.recommend.core.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}