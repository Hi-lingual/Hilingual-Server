package org.hilingual.domain.diary.api.service.diff.logic;

import org.hilingual.domain.diary.api.service.diff.data.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class DiffOperationMerger {

    public List<DiffOperation> groupConsecutiveChanges(List<DiffOperation> operations) {
        List<DiffOperation> grouped = new ArrayList<>();
        List<DiffOperation> currentGroup = new ArrayList<>();

        for (DiffOperation op : operations) {
            if (op.type() == DiffType.EQUAL) {
                if (!currentGroup.isEmpty()) {
                    grouped.add(mergeOperations(currentGroup));
                    currentGroup.clear();
                }
                grouped.add(op);
            } else {
                currentGroup.add(op);
            }
        }

        if (!currentGroup.isEmpty()) {
            grouped.add(mergeOperations(currentGroup));
        }

        return grouped;
    }

    private DiffOperation mergeOperations(List<DiffOperation> operations) {
        if (operations.size() == 1) {
            return operations.get(0);
        }

        StringBuilder mergedText = new StringBuilder();
        for (DiffOperation op : operations) {
            if (op.type() == DiffType.INSERT && op.rewriteWord() != null) {
                if (!mergedText.isEmpty()) {
                    mergedText.append(" ");
                }
                mergedText.append(op.rewriteWord().originalWord());
            }
        }

        return new DiffOperation(DiffType.INSERT, null,
                new WordInfo(mergedText.toString(), mergedText.toString(), 0, 0, false));
    }
}