package org.sopt.topic.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.topic.domian.Topic;
import org.sopt.topic.repository.TopicRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TopicRetriever {

    private final TopicRepository topicRepository;

    // TODO : TopicNotFoundException 예외처리 해줘야함
    public Optional<Topic> findByDate(LocalDate date) {
        return topicRepository.findByDate(date);
    }

}
