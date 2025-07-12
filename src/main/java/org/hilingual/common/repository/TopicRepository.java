package org.hilingual.common.repository;

import org.hilingual.common.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, LocalDate> {
    Optional<Topic> findByDate(LocalDate date);
}


