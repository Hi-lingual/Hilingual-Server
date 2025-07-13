package org.hilingual.domain.diaryfeedback.api.service.diff.logic;

import org.hilingual.domain.diaryfeedback.api.service.diff.data.WordInfo;

public record DiffOperation(
        DiffType type,
        WordInfo originalWord,
        WordInfo rewriteWord) {

}