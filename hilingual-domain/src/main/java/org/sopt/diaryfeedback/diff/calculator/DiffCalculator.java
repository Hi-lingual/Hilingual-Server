package org.sopt.diaryfeedback.diff.calculator;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.diff.comparator.WordComparator;
import org.sopt.diaryfeedback.diff.data.DiffOperation;
import org.sopt.diaryfeedback.diff.data.DiffType;
import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiffCalculator {

    private final WordComparator wordComparator;

    public List<DiffOperation> computeDiff(List<WordInfo> original, List<WordInfo> rewrite) {
        int originalSize = original.size();
        int rewriteSize = rewrite.size();

        int[][] dp = buildLcsTable(original, rewrite, originalSize, rewriteSize);
        return backtrackOperations(original, rewrite, dp, originalSize, rewriteSize);
    }

    private int[][] buildLcsTable(List<WordInfo> original,
                                  List<WordInfo> rewrite,
                                  int originalSize,
                                  int rewriteSize) {

        int[][] dp = new int[originalSize + 1][rewriteSize + 1];

        for (int i = 1; i <= originalSize; i++) {
            for (int j = 1; j <= rewriteSize; j++) {
                if (wordComparator.isSameWord(original.get(i - 1), rewrite.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp;
    }

    private List<DiffOperation> backtrackOperations(List<WordInfo> original,
                                                    List<WordInfo> rewrite,
                                                    int[][] dp,
                                                    int originalSize,
                                                    int rewriteSize) {

        List<DiffOperation> operations = new ArrayList<>();
        int i = originalSize;
        int j = rewriteSize;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0
                    && wordComparator.isSameWord(original.get(i - 1), rewrite.get(j - 1))) {

                operations.add(new DiffOperation(
                        DiffType.EQUAL,
                        original.get(i - 1),
                        rewrite.get(j - 1))
                );
                i--;
                j--;

            } else if (i > 0 && (j == 0 || dp[i - 1][j] >= dp[i][j - 1])) {

                operations.add(new DiffOperation(
                        DiffType.DELETE,
                        original.get(i - 1),
                        null)
                );
                i--;

            } else {

                operations.add(new DiffOperation(
                        DiffType.INSERT,
                        null,
                        rewrite.get(j - 1))
                );
                j--;
            }
        }

        // 역순으로 쌓았으므로 한 번 뒤집어서 반환
        Collections.reverse(operations);
        return operations;
    }
}
