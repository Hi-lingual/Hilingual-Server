package org.sopt.controller.diary.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.diary.dto.CreateDiaryReq;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryRes;
import org.sopt.controller.recommend.service.RecommendService;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.controller.diaryfeedback.service.DiaryFeedbackService;
import org.sopt.diaryfeedback.diff.service.DiaryDiffService;
import org.sopt.diaryfeedback.diff.prompt.DiaryFeedbackPrompt;
import org.sopt.openai.OpenAIService;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final OpenAIService openAiService;
    private final DiaryFeedbackService diaryFeedbackService;
    private final RecommendService recommendService;
    private final DiaryDiffService diaryDiffService;

    private final S3Service s3Service;

    @Transactional
    public DiaryRes createDiaryWithFeedback(
            Long userId,
            String originalText,
            LocalDate writtenDate,
            CreateDiaryReq.ImageRef imageRef
    ) {
        User user = userFacade.getUserById(userId);
        diaryFacade.validateNotExists(user, writtenDate);

        String finalImageKey = null;
        if (imageRef != null && imageRef.fileKey() != null && !imageRef.fileKey().isBlank()) {
            if (!"DIARY_IMAGE".equals(imageRef.purpose())) {
                throw new IllegalArgumentException("image.purpose must be DIARY_IMAGE");
            }
            finalImageKey = s3Service.bindDiaryImage(userId, imageRef.fileKey(), writtenDate);
        }

        var ai = openAiService.getDiaryFeedback(DiaryFeedbackPrompt.PROMPT, originalText);
        Diary diary = diaryFacade.saveDiary(
                user,
                originalText,
                ai.rewriteText(),
                finalImageKey,
                writtenDate
        );

        ai.feedbackList().stream()
                .map(f -> DiaryFeedback.create(diary, f.original(), f.rewrite(), f.explain()))
                .forEach(diaryFeedbackService::saveFeedback);

        ai.phraseList().stream()
                .map(p -> Recommend.create(diary, p.phrase(), String.join(",", p.phraseType()), p.explanation(), p.reason()))
                .forEach(recommendService::saveRecommend);

        return new DiaryRes(diary.getId());
    }

    public DiaryDetailsRes getDiaryDetails(final Long userId, final Long diaryId) {
        diaryFacade.validateDiaryOwnership(userId, diaryId);

        Diary diary = diaryFacade.getDiaryById(diaryId);

        String originalText = diary.getOriginalText();
        String rewriteText = diary.getRewriteText();
        String imageKey = diary.getImageUrl();

        String imageUrl = s3Service.toPublicUrl(imageKey);

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

    @Transactional
    public void removeDairy(final Long userId, final Long diary){
        diaryFacade.deleteDiary(userId, diary);
    };
}
