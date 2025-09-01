package org.sopt.feed.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feed.dto.FeedProjection;
import org.sopt.feed.repository.FeedRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedRetriever {

    private final FeedRepository feedRepository;

    public List<FeedProjection> findRecommendFeeds(final long userId, final Pageable pageable) {
        return feedRepository.findRecommendFeeds(userId, pageable);
    }

    public List<FeedProjection> findFollowingFeeds(final long userId, final Pageable pageable) {
        return feedRepository.findFollowingFeeds(userId, pageable);
    }
}
