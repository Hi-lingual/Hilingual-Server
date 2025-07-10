package org.hilingual.domain.recommend.api.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.api.dto.res.RecommendList;
import org.hilingual.domain.recommend.api.service.RecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/v1/diaries/{diaryId}/recommended")
    public ResponseEntity<RecommendList> getRecommendList(
            @RequestHeader("Authorization") Long userId,
            @PathVariable @Validated @Min(1) Long diaryId
    ){
        return ResponseEntity.ok(recommendService.getRecommendList(userId, diaryId));
    }

}
