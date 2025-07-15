package org.hilingual.domain.diaryfeedback.api.service.diff.calculator;

import org.hilingual.domain.diaryfeedback.api.service.diff.comparator.WordComparator;
import org.hilingual.domain.diaryfeedback.api.service.diff.data.DiffOperation;
import org.hilingual.domain.diaryfeedback.api.service.diff.data.DiffType;
import org.hilingual.domain.diaryfeedback.api.service.diff.data.WordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiffCalculator {

    private final WordComparator wordComparator;

    public DiffCalculator(WordComparator wordComparator) {
        this.wordComparator = wordComparator;
    }

    public List<DiffOperation> computeDiff(List<WordInfo> original, List<WordInfo> rewrite) {
        int m = original.size();
        int n = rewrite.size();

        int[][] dp = new int[m + 1][n + 1];

        // LCS 계산
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (wordComparator.isSameWord(original.get(i - 1), rewrite.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 역추적하여 diff 연산 생성
        List<DiffOperation> operations = new ArrayList<>();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && wordComparator.isSameWord(original.get(i - 1), rewrite.get(j - 1))) {
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
}
