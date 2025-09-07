package org.sopt.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendWithBookmarkDto {
    private Long id;
    private String phraseType;
    private String phrase;
    private String explanation;
    private String reason;
    private Boolean isBookmarked;
}