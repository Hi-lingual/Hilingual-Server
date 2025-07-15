package org.hilingual.domain.diaryfeedback.api.service.diff.data;


import lombok.Getter;

@Getter
public class DiffOperation {
    private final DiffType type;
    private final WordInfo originalWord;
    private final WordInfo rewriteWord;

    public DiffOperation(DiffType type, WordInfo originalWord, WordInfo rewriteWord) {
        this.type = type;
        this.originalWord = originalWord;
        this.rewriteWord = rewriteWord;
    }
}