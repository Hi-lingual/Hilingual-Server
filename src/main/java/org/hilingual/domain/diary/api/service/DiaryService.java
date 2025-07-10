package org.hilingual.domain.diary.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.api.dto.res.DiaryDetails;
import org.hilingual.domain.diary.api.dto.res.DiaryDto;
import org.hilingual.domain.diary.api.service.diff.DiaryFeedbackPrompt;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.facade.*;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.api.service.DiaryFeedbackService;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryValidator;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.api.service.RecommendService;
import org.hilingual.domain.user.api.service.UserService;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.diary.api.service.diff.DiaryDiffService;
import org.hilingual.external.openai.OpenAiService;
import org.hilingual.external.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final S3Service s3Service;
    private final OpenAiService openAiService;

    private final DiaryFeedbackService diaryFeedbackService;
    private final RecommendService recommendService;
    private final DiaryDiffService diaryDiffService;
    private final UserService userService;

    private final DiarySaver diarySaver;
    private final DiaryRetriever diaryRetriever;
    private final DiaryValidator diaryValidator;



    @Transactional
    public DiaryDto getFeedbacks(Long userId, String originalText, MultipartFile imageFile) {
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadImage("diaries", imageFile);
        }

        Map<String, Object> aiResponse = openAiService.getDiaryFeedback(DiaryFeedbackPrompt.PROMPT, originalText);

        String rewriteText = (String) aiResponse.get("rewriteText");
        List<Map<String, Object>> feedbackList = (List<Map<String, Object>>) aiResponse.get("feedbackList");
        List<Map<String, Object>> phraseList = (List<Map<String, Object>>) aiResponse.get("phraseList");

        User user = userService.findById(userId);
        Diary diary = diarySaver.save(user, originalText, rewriteText, imageUrl);

        feedbackList.stream()
                .map(f -> DiaryFeedback.create(
                        diary,
                        (String) f.get("original"),
                        (String) f.get("rewrite"),
                        (String) f.get("explain")
                ))
                .forEach(diaryFeedbackService::saveFeedback);

        phraseList.stream()
                .map(p -> Recommend.create(
                        diary,
                        (String) p.get("phrase"),
                        String.join(",", (List<String>) p.getOrDefault("phraseType", List.of())),
                        (String) p.get("explanation"),
                        (String) p.get("reason")
                ))
                .forEach(recommendService::saveRecommend);

        return new DiaryDto(diary.getId());
    }

    public DiaryDetails getDiaryDetails(final Long userId, final Long diaryId) {
        diaryValidator.validateDiaryOwnership(userId, diaryId);

        Diary diary = diaryRetriever.findById(diaryId);

        String originalText = diary.getOriginalText();
        String rewriteText = diary.getRewriteText();
        String imageUrl = diary.getImageUrl();

        String date = diary.getCreatedAt().format(DateTimeFormatter.ofPattern("M월 d일 EEEE"));

        List<DiaryDetails.DiffRange> diffRanges = diaryDiffService.extractDiffRanges(originalText, rewriteText);

        return DiaryDetails.builder()
                .date(date)
                .originalText(originalText)
                .rewriteText(rewriteText)
                .diffRanges(diffRanges)
                .imageUrl(imageUrl)
                .build();
    }
}