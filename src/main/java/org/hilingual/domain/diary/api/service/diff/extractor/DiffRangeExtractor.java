package org.hilingual.domain.diary.api.service.diff.extractor;

import org.hilingual.domain.diary.api.dto.res.DiaryDetails;
import org.hilingual.domain.diary.api.service.diff.data.WordInfo;
import org.hilingual.domain.diary.api.service.diff.logic.DiffOperation;

import java.util.ArrayList;
import java.util.List;

public class DiffRangeExtractor {

    public List<DiaryDetails.DiffRange> extractDiffRanges(List<DiffOperation> operations, String rewriteText) {
        List<DiaryDetails.DiffRange> diffRanges = new ArrayList<>();
        int currentPos = 0;

        for (DiffOperation operation : operations) {
            switch (operation.type()) {
                case INSERT -> {
                    WordInfo insertWord = operation.rewriteWord();
                    int start = currentPos;
                    int end = start + insertWord.originalWord().length();
                    diffRanges.add(new DiaryDetails.DiffRange(start, end, insertWord.originalWord()));
                    currentPos = end;
                }
                case EQUAL -> {
                    WordInfo equalWord = operation.rewriteWord();
                    currentPos += equalWord.originalWord().length();
                    if (currentPos < rewriteText.length() && rewriteText.charAt(currentPos) == ' ') {
                        currentPos++;
                    }
                }
                case DELETE -> {}
            }
        }

        return diffRanges;
    }
}