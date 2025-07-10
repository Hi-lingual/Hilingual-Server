package org.hilingual.domain.diary.api.service.diff;

import org.hilingual.domain.diary.api.dto.res.DiaryDetails;
import org.hilingual.domain.diary.api.service.diff.data.WordExtractor;
import org.hilingual.domain.diary.api.service.diff.data.WordInfo;
import org.hilingual.domain.diary.api.service.diff.extractor.DiffRangeExtractor;
import org.hilingual.domain.diary.api.service.diff.logic.DiffOperation;
import org.hilingual.domain.diary.api.service.diff.logic.DiffOperationMerger;
import org.hilingual.domain.diary.api.service.diff.logic.DiffProcessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryDiffService {

    private final WordExtractor wordExtractor;
    private final DiffProcessor diffProcessor;
    private final DiffOperationMerger diffOperationMerger;
    private final DiffRangeExtractor diffRangeExtractor;

    public DiaryDiffService() {
        this.wordExtractor = new WordExtractor();
        this.diffProcessor= new DiffProcessor();
        this.diffOperationMerger = new DiffOperationMerger();
        this.diffRangeExtractor = new DiffRangeExtractor();
    }

    public List<DiaryDetails.DiffRange> extractDiffRanges(String originalText, String rewriteText) {
        if (originalText == null || rewriteText == null) {
            return new ArrayList<>();
        }

        List<WordInfo> originalWords = wordExtractor.extractWordsWithPosition(originalText);
        List<WordInfo> rewriteWords = wordExtractor.extractWordsWithPosition(rewriteText);

        List<DiffOperation> operations = diffProcessor.computeDiff(originalWords, rewriteWords);
        List<DiffOperation> groupedOperations = diffOperationMerger.groupConsecutiveChanges(operations);

        return diffRangeExtractor.extractDiffRanges(groupedOperations, rewriteText);
    }
}