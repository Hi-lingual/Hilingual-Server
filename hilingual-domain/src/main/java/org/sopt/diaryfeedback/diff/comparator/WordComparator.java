package org.sopt.diaryfeedback.diff.comparator;

import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.springframework.stereotype.Component;

@Component
public class WordComparator {

    public boolean isSameWord(WordInfo w1, WordInfo w2) {
        if (isKorean(w1.getCleanWord()) != isKorean(w2.getCleanWord())) return false;
        return w1.getOriginalWord().equals(w2.getOriginalWord());
    }

    private boolean isKorean(String s) {
        if (s == null || s.isEmpty()) return false;
        return s.codePoints().anyMatch(ch -> ch >= 0xAC00 && ch <= 0xD7A3);
    }
}