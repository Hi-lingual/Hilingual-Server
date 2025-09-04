package org.sopt.controller.feed.dto;

public record LikeToggleRes(boolean isLiked) {
    public static LikeToggleRes of(boolean isLiked) {
        return new LikeToggleRes(isLiked);
    }
}