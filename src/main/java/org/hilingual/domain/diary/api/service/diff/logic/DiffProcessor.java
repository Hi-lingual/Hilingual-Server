package org.hilingual.domain.diary.api.service.diff.logic;

import org.hilingual.domain.diary.api.service.diff.data.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class DiffProcessor {

    public List<DiffOperation> computeDiff(List<WordInfo> original, List<WordInfo> rewrite) {
        int m = original.size();
        int n = rewrite.size();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (isSameWord(original.get(i - 1), rewrite.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        List<DiffOperation> operations = new ArrayList<>();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && isSameWord(original.get(i - 1), rewrite.get(j - 1))) {
                operations.add(0, new DiffOperation(DiffType.EQUAL, original.get(i - 1), rewrite.get(j - 1)));
                i--;
                j--;
            } else if (i > 0 && (j == 0 || dp[i - 1][j] >= dp[i][j - 1])) {
                operations.add(0, new DiffOperation(DiffType.DELETE, original.get(i - 1), null));
                i--;
            } else {
                operations.add(0, new DiffOperation(DiffType.INSERT, null, rewrite.get(j - 1)));
                j--;
            }
        }

        return operations;
    }

    private boolean isSameWord(WordInfo word1, WordInfo word2) {
        if (isKorean(word1.cleanWord()) && !isKorean(word2.cleanWord())) {
            return false;
        }
        if (word1.hasPunctuation() != word2.hasPunctuation()) {
            return false;
        }
        return word1.cleanWord().equals(word2.cleanWord());
    }

    private boolean isKorean(String text) {
        return text.chars().anyMatch(ch -> ch >= 0xAC00 && ch <= 0xD7A3);
    }
}