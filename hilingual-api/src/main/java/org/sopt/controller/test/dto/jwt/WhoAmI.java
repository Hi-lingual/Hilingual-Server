package org.sopt.controller.test.dto.jwt;

import lombok.Builder;

@Builder
public record WhoAmI(
        Long userId,
        String role
) {
}