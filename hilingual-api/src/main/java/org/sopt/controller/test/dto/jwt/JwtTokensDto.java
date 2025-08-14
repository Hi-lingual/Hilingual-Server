package org.sopt.controller.test.dto.jwt;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken,
        String refreshToken
) { }