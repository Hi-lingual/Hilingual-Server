package org.hilingual.domain.voca.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
import org.hilingual.domain.voca.api.exception.VocaInvalidSortTypeException;
import org.hilingual.domain.voca.api.service.VocaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voca")
@RequiredArgsConstructor
public class VocaController {

    private final VocaService vocaService;

    @GetMapping
    public ResponseEntity<VocaListResponse> getVocaList(
            @RequestParam(value = "sort", required = false, defaultValue = "1") final String sortStr
    ) {
        final int sort;
        try {
            sort = Integer.parseInt(sortStr);
        } catch (NumberFormatException e) {
            throw new VocaInvalidSortTypeException();
        }

        if (sort != 1 && sort != 2) {
            throw new VocaInvalidSortTypeException();
        }

        Long userId = 1L; // TODO: 로그인 연동 후 수정
        return ResponseEntity.ok(vocaService.getVocaList(userId, sort));
    }
}
