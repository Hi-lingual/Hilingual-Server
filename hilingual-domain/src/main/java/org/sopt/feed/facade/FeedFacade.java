package org.sopt.feed.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feed.dto.FeedProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedFacade {

    private final FeedRetriever feedRetriever;

    public List<FeedProjection> findRecommendFeeds(
            final long userId,
            Pageable pageable
    ) {
        return feedRetriever.findRecommendFeeds(userId, pageable);
    }
}
