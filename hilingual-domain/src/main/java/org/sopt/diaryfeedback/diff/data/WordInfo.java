package org.sopt.diaryfeedback.diff.data;

import lombok.Getter;

@Getter
public class WordInfo {
    private final String cleanWord;
    private final String originalWord;
    private final int start;
    private final int end;
    private final boolean hasPunctuation;

    public WordInfo(String cleanWord, String originalWord, int start, int end, boolean hasPunctuation) {
        this.cleanWord = cleanWord;
        this.originalWord = originalWord;
        this.start = start;
        this.end = end;
        this.hasPunctuation = hasPunctuation;
    }
    public boolean hasPunctuation() { return hasPunctuation; }
}
