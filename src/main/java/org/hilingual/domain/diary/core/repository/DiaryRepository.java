package org.hilingual.domain.diary.core.repository;

import org.hilingual.domain.diary.core.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}