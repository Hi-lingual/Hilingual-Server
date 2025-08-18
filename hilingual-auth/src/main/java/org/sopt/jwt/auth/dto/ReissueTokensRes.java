package org.sopt.jwt.auth.dto;

import lombok.Builder;

@Builder
public record ReissueTokensRes(
        String accessToken,
        String refreshToken
){
}