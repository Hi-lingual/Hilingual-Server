package org.hilingual.domain.diary.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.api.dto.res.DiaryDto;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.facade.*;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.api.service.DiaryFeedbackService;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.api.service.RecommendService;
import org.hilingual.domain.user.api.service.UserService;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.external.openai.OpenAiService;
import org.hilingual.external.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final UserService userService;
    private final DiarySaver diarySaver;

    private static final String DIARY_FEEDBACK_PROMPT =
            "**1️⃣ 철자와 문법 오류만 교정한 전체 일기를 반환하세요. (rewriteText)**\n\n" +
                    "- 오직 문법(Grammar) 및 철자(Spelling) 오류만 교정해주세요.\n" +
                    "- 교정할 오류가 없다면 원문을 그대로 반환하세요.\n" +
                    "- 일기 중 한국어로 작성한 부분은 영어로 번역해주세요.\n\n" +
                    "**2️⃣ 철자 또는 문법 오류가 있었던 문장들(최대 5개)을 반환하세요. (feedbackList)**\n\n" +
                    "- 각 문장마다 다음 정보를 반드시 포함하세요:\n" +
                    "  \n" +
                    "  original : 틀린 문장 (사용자가 작성한 원문) → rewrite : 교정된 문장\n" +
                    "  \n" +
                    "  explain : 교정 이유 + 간단한 배경 설명 (한국어로 1~2문장)\n\n" +
                    "- explain 작성 방법:\n" +
                    "  - 단순히 틀렸다고 말하지 말고, 왜 틀렸는지 정확히 알려주세요.\n" +
                    "  - 가능하면 문법 용어 + 맥락에 따른 이유까지 알려주세요.\n" +
                    "  - 너무 딱딱하지 않고, 친절한 한국어 표현으로 작성하세요.\n" +
                    "- 너무 사소한 수정(단순 대소문자 변경, 하이픈 추가/삭제 등)은 제외하세요.\n" +
                    "- 일기 중 한국어로 작성한 부분 번역은 설명에서 제외하세요.\n\n" +
                    "예시:\n" +
                    "I think I have a lot of people who gave positive impact on my life.\n" +
                    "→ I think I have a lot of people who have had a positive impact on my life.\n\n" +
                    "“현재까지 영향을 미친 사람들을 말하고 있으므로 현재완료형 ‘have had’를 사용해야 자연스럽습니다.”\n\n" +
                    "**3️⃣ 추천할 영어 표현(최소 5개 ~ 최대 8개)을 반환하세요. (phraseList)**\n\n" +
                    "- 사용자가 작성한 표현은 제외하고, 일기 내용과 어울리는 새로운 표현만 추천하세요.\n" +
                    "- 각 표현마다 아래 정보를 반드시 포함하세요:\n" +
                    "  - phrase : 추천 표현 (영어)\n" +
                    "  - phraseType : 품사 목록 (영어 8품사: 명사, 대명사, 동사, 형용사, 부사, 전치사, 접속사, 감탄사)\n" +
                    "  - 필요시 추가 속성 (최대 1개까지, 없으면 제외):\n" +
                    "    - 숙어 (관용표현, 관용어 포함)\n" +
                    "    - 속어 (슬랭, 신조어, 유행어 포함)\n" +
                    "    - 구 (2단어 이상 결합된 경우)\n" +
                    "  - explanation : 추천 표현의 한국어 의미 (자연스럽고 정확하게 작성)\n" +
                    "  - reason : 이 표현이 사용된 예문을 포함하여 해당 일기에 적합한 이유를 알려주세요. 예문은 큰 따옴표를 표시해주세요.\n\n" +
                    "예시:\n" +
                    "phrase: resonate with\n" +
                    "phraseType: 동사 (숙어)\n" +
                    "explanation: ~와 깊이 공감되다, 마음에 와닿다\n" +
                    "reason: “Their lyrics really resonate with me.”처럼 사용하면 감정적인 연결을 강조할 수 있어요.\n\n" +
                    "4️⃣ 결과는 반드시 아래 JSON 형식을 지켜서 반환하세요:\n\n" +
                    "{\n" +
                    "  \"rewriteText\": \"교정된 전체 일기 (또는 원문 그대로)\",\n" +
                    "  \"feedbackList\": [\n" +
                    "    {\n" +
                    "      \"original\": \"틀린 문장\",\n" +
                    "      \"rewrite\": \"교정된 문장\",\n" +
                    "      \"explain\": \"교정 이유 (한국어)\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"phraseList\": [\n" +
                    "    {\n" +
                    "      \"phrase\": \"추천 표현\",\n" +
                    "      \"phraseType\": [\"동사\", \"숙어\"],\n" +
                    "      \"explanation\": \"추천 표현에 대한 한국어 설명\",\n" +
                    "      \"reason\": \"해당 표현을 추천한 이유\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n\n" +
                    "- 반드시 지켜야 할 주의사항\n" +
                    "- JSON 형식을 반드시 지키세요.\n" +
                    "- 필수 key 중 하나라도 누락하지 마세요.\n" +
                    "- feedbackList는 최대 5개까지만, phraseList는 5~8개 반드시 제공하세요.\n" +
                    "- 표현 추천에서는 사용자가 쓴 표현은 절대 중복 추천하지 마세요.";

    @Transactional
    public DiaryDto getFeedbacks(Long userId, String originalText, MultipartFile imageFile) {
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadImage("diaries", imageFile);
        }

        Map<String, Object> aiResponse = openAiService.getDiaryFeedback(DIARY_FEEDBACK_PROMPT, originalText);

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
}