package org.hilingual.domain.recommend.core.repository;

import org.hilingual.domain.recommend.core.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< feat/31
import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    List<Recommend> findByDiaryId(Long diaryId);

=======
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
>>>>>>> develop
}