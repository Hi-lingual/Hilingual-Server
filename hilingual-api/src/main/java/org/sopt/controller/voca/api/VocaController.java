package org.sopt.controller.voca.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.voca.dto.req.VocaMemorizationUpdateRequest;
import org.sopt.controller.voca.dto.res.VocaDetailResponse;
import org.sopt.jwt.annotation.UserId;
import org.sopt.voca.dto.VocaListRes;
import org.sopt.controller.voca.dto.res.VocaSearchListResponse;
import org.sopt.controller.voca.exception.VocaApiErrorCode;
import org.sopt.controller.voca.exception.VocaInvalidKeywordException;
import org.sopt.controller.voca.exception.VocaInvalidSortTypeException;
import org.sopt.controller.voca.service.VocaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voca")
@RequiredArgsConstructor
public class VocaController {

    private final VocaService vocaService;

    // 단어장 목록 조회
    @GetMapping
    public ResponseEntity<VocaListRes> getVocaList(
            @UserId Long userId,
            @RequestParam(value = "sort", required = false, defaultValue = "1") final String sortStr,
            @RequestParam(value = "unmemorizedOnly", required = false, defaultValue = "false") final boolean unmemorizedOnly
    ) {
        int sort = parseAndValidateSortType(sortStr);
        return ResponseEntity.ok(vocaService.getVocaList(userId, sort, unmemorizedOnly));
    }

    // 단어장 검색
    @GetMapping("/search")
    public ResponseEntity<VocaSearchListResponse> searchVocaList(
            @UserId Long userId,
            @RequestParam final String keyword
    ) {
        final String trimmedKeyword = keyword.trim();
        validateKeyword(trimmedKeyword);

        return ResponseEntity.ok(vocaService.searchVocaList(userId, trimmedKeyword));
    }

    // 특정 단어 세부 조회
    @GetMapping("/{phraseId}")
    public ResponseEntity<VocaDetailResponse> getVocaDetails(
            @UserId Long userId,
            @PathVariable final Long phraseId
    ) {
        return ResponseEntity.ok(vocaService.getVocaDetails(userId, phraseId));
    }

    //단어 외움 상태 일괄 수정
    @PatchMapping("/memorization")
    public ResponseEntity<Void> updateMemorization(
            @UserId Long userId,
            @Valid @RequestBody final VocaMemorizationUpdateRequest request
            ){
        vocaService.updateMemorization(userId, request);
        return ResponseEntity.ok().build();
    }


    private int parseAndValidateSortType(String sortStr) {
        try {
            int sort = Integer.parseInt(sortStr);
            if (sort != 1 && sort != 2) {
                throw new VocaInvalidSortTypeException(VocaApiErrorCode.INVALID_SORT_TYPE);
            }
            return sort;
        } catch (NumberFormatException e) {
            throw new VocaInvalidSortTypeException(VocaApiErrorCode.INVALID_SORT_TYPE);
        }
    }

    private void validateKeyword(String keyword) {
        if (keyword.isBlank()) {
            throw new VocaInvalidKeywordException(VocaApiErrorCode.INVALID_KEYWORD);
        }
    }

}
