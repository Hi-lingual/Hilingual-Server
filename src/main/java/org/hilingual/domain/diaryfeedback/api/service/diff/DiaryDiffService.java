package org.hilingual.domain.diaryfeedback.api.service.diff;

import org.hilingual.domain.diary.api.dto.res.DiaryDetails;
import org.hilingual.domain.diaryfeedback.api.service.diff.builder.DiffRangeBuilder;
import org.hilingual.domain.diaryfeedback.api.service.diff.calculator.DiffCalculator;
import org.hilingual.domain.diaryfeedback.api.service.diff.data.DiffOperation;
import org.hilingual.domain.diaryfeedback.api.service.diff.data.WordInfo;
import org.hilingual.domain.diaryfeedback.api.service.diff.parser.TextParser;
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

    public List<DiaryDetails.DiffRange> extractDiffRanges(String originalText, String rewriteText) {
        if (originalText == null || rewriteText == null) {
            return new ArrayList<>();
        }

        List<WordInfo> originalWords = textParser.extractWordsWithPosition(originalText);
        List<WordInfo> rewriteWords = textParser.extractWordsWithPosition(rewriteText);

        List<DiffOperation> operations = diffCalculator.computeDiff(originalWords, rewriteWords);

        return diffRangeBuilder.buildDiffRanges(operations, rewriteText);
    }
}