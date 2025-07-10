package org.hilingual.domain.voca.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.api.dto.res.VocaListResponse;
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
            @RequestParam(value = "sort", required = false, defaultValue = "1") final int sort
    ) {

        Long userId = 1L; // TODO: 나중에 로그인 적용되면 userId 주입
        VocaListResponse response = vocaService.getVocaList(userId, sort);
        return ResponseEntity.ok(response);
    }
}
