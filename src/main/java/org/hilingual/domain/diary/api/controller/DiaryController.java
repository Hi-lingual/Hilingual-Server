package org.hilingual.domain.diary.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.api.service.DiaryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

}