package org.sopt.controller.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleTokenRes(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("error") String error,
        @JsonProperty("error_description") String errorDescription
) {
}
