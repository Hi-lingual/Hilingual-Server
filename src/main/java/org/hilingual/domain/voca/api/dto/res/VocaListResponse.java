package org.hilingual.domain.voca.api.dto.res;

import java.util.List;

public record VocaListResponse(
        int count,
        List<WordGroup> wordList
) {}
