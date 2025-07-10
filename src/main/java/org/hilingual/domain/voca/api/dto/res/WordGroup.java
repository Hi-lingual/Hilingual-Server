package org.hilingual.domain.voca.api.dto.res;

import java.util.List;

public record WordGroup(
        String group,
        List<VocaItemResponse> words
) {}
