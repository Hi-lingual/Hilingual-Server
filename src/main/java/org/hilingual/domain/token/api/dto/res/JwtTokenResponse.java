package org.hilingual.domain.token.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
    private Boolean isProfileCompleted;

    public static JwtTokenResponse of(String accessToken, String refreshToken, Boolean isProfileCompleted) {
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isProfileCompleted(isProfileCompleted)
                .build();
    }
}