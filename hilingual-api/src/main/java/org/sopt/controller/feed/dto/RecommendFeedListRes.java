package org.sopt.controller.feed.dto;

import java.util.List;

public record RecommendFeedListRes(
        List<RecommendFeed> diaryList
) {
    public record RecommendFeed(
            RecommendFeedProfile profile,
            RecommendFeedDiary diary
    ) {
    }

    public record RecommendFeedProfile(
            Long userId,
            Boolean isMine,
            String profileImg,
            String nickname,
            Integer streak
    ) {
    }

    public record RecommendFeedDiary(
            Long diaryId,
            Long sharedDate,
            Integer likeCount,
            Boolean isLiked,
            String diaryImg,
            String originalText
    ) {
    }
}