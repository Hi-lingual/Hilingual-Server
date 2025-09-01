package org.sopt.controller.feed.dto;

import java.util.Collections;
import java.util.List;

public record FollowFeedListRes(
        List<FollowFeed> diaryList,
        Boolean haveFollowing
) {
    public static FollowFeedListRes empty() {
        return new FollowFeedListRes(Collections.emptyList(), false);
    }

    public record FollowFeed(
            FollowFeedProfile profile,
            FollowFeedDiary diary
    ) {
    }

    public record FollowFeedProfile(
            Long userId,
            Boolean isMine,
            String profileImg,
            String nickname,
            Integer streak
    ) {
    }

    public record FollowFeedDiary(
            Long diaryId,
            Long sharedDate,
            Integer likeCount,
            Boolean isLiked,
            String diaryImg,
            String originalText
    ) {
    }
}