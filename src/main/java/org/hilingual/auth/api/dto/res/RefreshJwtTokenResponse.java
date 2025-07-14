package org.hilingual.auth.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtTokenResponse {
    private String accessToken;
    private String refreshToken;

    public static RefreshJwtTokenResponse of(String accessToken, String refreshToken) {
        return RefreshJwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
