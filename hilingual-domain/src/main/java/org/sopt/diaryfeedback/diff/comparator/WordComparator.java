package org.sopt.diaryfeedback.diff.comparator;

import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.springframework.stereotype.Component;

@Component
public class WordComparator {

    public boolean isSameWord(WordInfo word1, WordInfo word2) {
        if (isKorean(word1.getCleanWord()) && !isKorean(word2.getCleanWord())) {
            return false;
        }
        if (word1.hasPunctuation() != word2.hasPunctuation()) {
            return false;
        }
        return word1.getCleanWord().equals(word2.getCleanWord());
    }

    private boolean isKorean(String text) {
        return text.chars().anyMatch(ch -> ch >= 0xAC00 && ch <= 0xD7A3);
    }
}