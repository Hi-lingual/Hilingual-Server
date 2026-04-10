package org.sopt.controller.diary.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.dto.Purpose;
import lombok.extern.slf4j.Slf4j;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.diary.dto.CreateDiaryReq;
import org.sopt.controller.diary.exception.DiaryApiErrorCode;
import org.sopt.controller.diary.exception.DiaryImagePurposeMismatchException;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryRes;
import org.sopt.controller.recommend.service.RecommendService;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.controller.diaryfeedback.service.DiaryFeedbackService;
import org.sopt.diaryfeedback.diff.service.DiaryDiffService;
import org.sopt.diaryfeedback.diff.prompt.DiaryFeedbackPrompt;
import org.sopt.likeddiary.facade.LikedDiaryFacade;
import org.sopt.openai.OpenAIService;
import org.sopt.openai.dto.res.GptFeedbackResponse;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final UserFacade userFacade;
    private final DiaryFacade diaryFacade;
    private final LikedDiaryFacade likedDiaryFacade;
    private final OpenAIService openAiService;
    private final DiaryFeedbackService diaryFeedbackService;
    private final RecommendService recommendService;
    private final DiaryDiffService diaryDiffService;
    private final UserCalendarFacade userCalendarFacade;
    private final UserProfileFacade userProfileFacade;

    private final S3Service s3Service;

    // 일기 피드백 요청
    @Transactional
    public DiaryRes createDiaryWithFeedback(
            Long userId,
            String originalText,
            LocalDate writtenDate,
            CreateDiaryReq.ImageRef imageRef,
            ZoneId userZone
    ) {
        User user = userFacade.getUserById(userId);

        user.setPrimaryTimezone(userZone.getId());
        diaryFacade.validateNotExists(user, writtenDate);

        String fileKey = bindDiaryImageIfPresent(userId, writtenDate, imageRef);

        var ai = openAiService.getDiaryFeedback(DiaryFeedbackPrompt.PROMPT, originalText);
        Diary diary = diaryFacade.saveDiary(
                user,
                originalText,
                ai.rewriteText(),
                fileKey,
                writtenDate,
                userZone
        );

        saveFeedbacks(diary, ai.feedbackList());
        saveRecommends(diary, ai.phraseList());

        return new DiaryRes(diary.getId(), diary.getIsAdWatched());
    }

    private String bindDiaryImageIfPresent(Long userId, LocalDate writtenDate, CreateDiaryReq.ImageRef imageRef) {
        if (imageRef == null) return null;
        if (imageRef.purpose() != Purpose.DIARY_IMAGE) {
            throw new DiaryImagePurposeMismatchException(DiaryApiErrorCode.IMAGE_PURPOSE_INVALID);
        }
        return s3Service.bindDiaryImage(userId, imageRef.fileKey(), writtenDate);
    }


    private void saveFeedbacks(Diary diary, List<GptFeedbackResponse.Feedback> feedbackList) {
        feedbackList.stream()
                .map(f -> DiaryFeedback.create(diary, f.original(), f.rewrite(), f.explain()))
                .forEach(diaryFeedbackService::saveFeedback);
    }

    private void saveRecommends(Diary diary, List<GptFeedbackResponse.Phrase> phraseList) {
        phraseList.stream()
                .map(p -> Recommend.create(
                        diary,
                        p.phrase(),
                        String.join(",", p.phraseType()),
                        p.explanation(),
                        p.reason()
                ))
                .forEach(recommendService::saveRecommend);
    }

    // 일기 상세조회
    public DiaryDetailsRes getDiaryDetails(final Long userId, final Long diaryId) {
        diaryFacade.validateReadable(userId, diaryId);

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
                .isPublished(diary.getIsPublic())
                .isAdWatched(diary.getIsAdWatched())
                .build();
    }

    @Transactional
    public void publishDiary(final Long userId, final Long diaryId) {
        diaryFacade.publish(userId, diaryId);
    }

    @Transactional
    public void unpublishDiary(final Long userId, final Long diaryId) {
        diaryFacade.unpublish(userId, diaryId);

        likedDiaryFacade.unlikeAllOfDiary(diaryId);

    }

    @Transactional
    public void markAdWatched(final Long userId, final Long diaryId) {
        diaryFacade.markAdWatched(userId, diaryId);
    }
}
