package org.sopt.controller.diary.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.utils.S3Service;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryDtoRes;
import org.sopt.controller.recommend.service.RecommendService;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.controller.diaryfeedback.service.DiaryFeedbackService;
import org.sopt.diaryfeedback.diff.service.DiaryDiffService;
import org.sopt.diaryfeedback.diff.prompt.DiaryFeedbackPrompt;
import org.sopt.openai.OpenAIService;
import org.sopt.openai.dto.res.GptFeedbackResponse;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final UserFacade userFacade;
    private final DiaryFacade diaryFacade;

    private final S3Service s3Service;
    private final OpenAIService openAiService;

    private final DiaryFeedbackService diaryFeedbackService;
    private final RecommendService recommendService;

    private final DiaryDiffService diaryDiffService;

    private static final String S3_BASE_URL = "https://hilingual-bucket.s3.ap-northeast-2.amazonaws.com/";

    @Transactional
    public DiaryDtoRes getFeedbacks(Long userId, String originalText, LocalDate writtenDate, MultipartFile imageFile) {
        User user = userFacade.getUserById(userId);
        diaryFacade.validateNotExists(user, writtenDate);

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadImage("diaries", imageFile);
        }

        GptFeedbackResponse aiResponse = openAiService.getDiaryFeedback(DiaryFeedbackPrompt.PROMPT, originalText);

        String rewriteText = aiResponse.rewriteText();
        List<GptFeedbackResponse.Feedback> feedbackList = aiResponse.feedbackList();
        List<GptFeedbackResponse.Phrase> phraseList = aiResponse.phraseList();

        Diary diary = diaryFacade.saveDiary(
                user,
                originalText,
                aiResponse.rewriteText(),
                imageUrl,
                writtenDate
        );

        feedbackList.stream()
                .map(f -> DiaryFeedback.create(diary, f.original(), f.rewrite(), f.explain()))
                .forEach(diaryFeedbackService::saveFeedback);

        phraseList.stream()
                .map(p -> Recommend.create(diary, p.phrase(), String.join(",", p.phraseType()), p.explanation(), p.reason()))
                .forEach(recommendService::saveRecommend);

        return new DiaryDtoRes(diary.getId());
    }

    public DiaryDetailsRes getDiaryDetails(final Long userId, final Long diaryId) {
        diaryFacade.validateDiaryOwnership(userId, diaryId);

        Diary diary = diaryFacade.getDiaryById(diaryId);

        String originalText = diary.getOriginalText();
        String rewriteText = diary.getRewriteText();
        String imageUrlKey = diary.getImageUrl();

        String imageUrl = (imageUrlKey != null && !imageUrlKey.isBlank())
                ? S3_BASE_URL + imageUrlKey
                : null;

        String date = diary.getWrittenDate()
                .format(DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN));

        List<DiaryDetailsRes.DiffRange> diffRanges = diaryDiffService.extractDiffRanges(originalText, rewriteText);

        return DiaryDetailsRes.builder()
                .date(date)
                .originalText(originalText)
                .rewriteText(rewriteText)
                .diffRanges(diffRanges)
                .imageUrl(imageUrl)
                .build();
    }
}
