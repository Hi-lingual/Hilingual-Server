package org.hilingual.external.openai.dto.res;

import java.util.List;

public record GptFeedbackResponse(
        String rewriteText,
        List<Feedback> feedbackList,
        List<Phrase> phraseList
) {
    public record Feedback(
            String original,
            String rewrite,
            String explain
    ) {}

    public record Phrase(
            String phrase,
            List<String> phraseType,
            String explanation,
            String reason
    ) {}
}