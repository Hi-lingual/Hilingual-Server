package org.sopt.diaryfeedback.diff.data;

public record WordInfo(
        String cleanWord,
        String originalWord,
        int start,
        int end,
        boolean hasPunctuation
) {
}
