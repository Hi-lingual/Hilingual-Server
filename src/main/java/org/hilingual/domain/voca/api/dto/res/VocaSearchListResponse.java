package org.hilingual.domain.voca.api.dto.res;

import java.util.List;

public record VocaSearchListResponse(
        List<VocaSearchResponse> searchList
) {}
