package org.sopt.topic.repository;

import org.sopt.topic.domian.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, LocalDate> {

    Optional<Topic> findByDate(LocalDate date);

}