package org.sopt.diaryfeedback.diff.data;

public record DiffOperation(
        DiffType type,
        WordInfo originalWord,
        WordInfo rewriteWord
) {
}
