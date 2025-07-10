package org.hilingual.domain.diary.api.service.diff.data;

public record WordInfo(
        String cleanWord,
        String originalWord,
        int start,
        int end,
        boolean hasPunctuation
) {

}