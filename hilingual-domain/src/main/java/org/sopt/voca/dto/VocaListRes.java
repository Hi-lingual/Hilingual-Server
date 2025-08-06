package org.sopt.voca.dto;

import java.util.List;

public record VocaListRes(
        int count,
        List<WordGroup> wordList
) {
    public record WordGroup(
            String group,
            List<VocaItemRes> words
    ) {}
}
