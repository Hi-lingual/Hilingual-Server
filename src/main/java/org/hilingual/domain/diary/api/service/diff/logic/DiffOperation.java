package org.hilingual.domain.diary.api.service.diff.logic;

import org.hilingual.domain.diary.api.service.diff.data.WordInfo;

public record DiffOperation(
        DiffType type,
        WordInfo originalWord,
        WordInfo rewriteWord) {

}