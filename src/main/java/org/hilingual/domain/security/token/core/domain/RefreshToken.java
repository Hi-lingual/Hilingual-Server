package org.hilingual.domain.security.token.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    private Long userId;
    private String refreshToken;
    private Long expirationTimestamp;
}
