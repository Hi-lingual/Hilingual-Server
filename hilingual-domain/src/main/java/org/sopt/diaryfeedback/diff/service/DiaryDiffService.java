package org.sopt.diaryfeedback.diff.service;

import org.sopt.diaryfeedback.diff.calculator.DiffCalculator;
import org.sopt.diaryfeedback.diff.builder.DiffRangeBuilder;
import org.sopt.diaryfeedback.diff.data.DiffOperation;
import org.sopt.diaryfeedback.diff.data.WordInfo;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.diaryfeedback.diff.parser.TextParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryDiffService {

    private final TextParser textParser;
    private final DiffCalculator diffCalculator;
    private final DiffRangeBuilder diffRangeBuilder;

    public DiaryDiffService(TextParser textParser,
                            DiffCalculator diffCalculator,
                            DiffRangeBuilder diffRangeBuilder) {
        this.textParser = textParser;
        this.diffCalculator = diffCalculator;
        this.diffRangeBuilder = diffRangeBuilder;
    }

    public List<DiaryDetailsRes.DiffRange> extractDiffRanges(String originalText, String rewriteText) {
        if (originalText == null || rewriteText == null) {
            return new ArrayList<>();
        }

        List<WordInfo> originalWords = textParser.extractWordsWithPosition(originalText);
        List<WordInfo> rewriteWords = textParser.extractWordsWithPosition(rewriteText);

        List<DiffOperation> operations = diffCalculator.computeDiff(originalWords, rewriteWords);

        return diffRangeBuilder.buildDiffRanges(operations, rewriteText);
    }
}