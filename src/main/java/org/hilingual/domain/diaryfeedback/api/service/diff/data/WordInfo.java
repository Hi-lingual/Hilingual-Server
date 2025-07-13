package org.hilingual.domain.diaryfeedback.api.service.diff.data;

public record WordInfo(
        String cleanWord,
        String originalWord,
        int start,
        int end,
        boolean hasPunctuation
) {

}
