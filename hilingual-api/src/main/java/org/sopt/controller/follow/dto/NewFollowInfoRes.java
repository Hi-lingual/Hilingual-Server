package org.sopt.controller.follow.dto;

import lombok.Builder;

@Builder
public record NewFollowInfoRes(
        boolean followedBy
) {
    public static NewFollowInfoRes of(boolean followedBy) {
        return NewFollowInfoRes.builder()
                .followedBy(followedBy)
                .build();
    }
}