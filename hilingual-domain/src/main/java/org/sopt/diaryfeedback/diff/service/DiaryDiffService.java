package org.sopt.diaryfeedback.diff.service;

import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.diff.calculator.DiffCalculator;
import org.sopt.diaryfeedback.diff.builder.DiffRangeBuilder;
import org.sopt.diaryfeedback.diff.data.DiffOperation;
import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.diaryfeedback.diff.parser.TextParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryDiffService {

    private final TextParser textParser;
    private final DiffCalculator diffCalculator;
    private final DiffRangeBuilder diffRangeBuilder;

    public List<DiaryDetailsRes.DiffRange> extractDiffRanges(String originalText, String rewriteText) {
        if (originalText == null || rewriteText == null) return List.of();
        if (rewriteText.isBlank()) return List.of(); // 표시할 대상이 없으면 종료

        List<WordInfo> originalWords = textParser.extractWordsWithPosition(originalText);
        List<WordInfo> rewriteWords  = textParser.extractWordsWithPosition(rewriteText);

        List<DiffOperation> operations = diffCalculator.computeDiff(originalWords, rewriteWords);

        List<DiaryDetailsRes.DiffRange> ranges = diffRangeBuilder.buildDiffRanges(operations, rewriteText);

        final int L = rewriteText.length();
        List<DiaryDetailsRes.DiffRange> safe = new ArrayList<>(ranges.size());
        for (DiaryDetailsRes.DiffRange r : ranges) {
            if (r.start() < 0) continue;
            if (r.end() > L) continue;
            if (r.start() >= r.end()) continue;
            safe.add(r);
        }
        return safe;
    }
}
