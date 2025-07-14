package org.hilingual.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hilingual.auth.api.dto.req.AuthRequest;
import org.hilingual.auth.api.dto.res.RefreshJwtTokenResponse;
import org.hilingual.auth.api.service.AuthService;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.hilingual.domain.token.api.service.RefreshTokenService;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.hilingual.auth.api.constant.AuthConstants.PROVIDER_APPLE;
import static org.hilingual.auth.api.constant.AuthConstants.PROVIDER_GOOGLE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/v1/auth/login")
    public BaseResponseDto<JwtTokenResponse> socialLogin(
            @RequestHeader("Provider-Token") String providerToken,
            @RequestBody AuthRequest authRequest
    ) {
        JwtTokenResponse responseData = authService.socialLogin(authRequest.provider(), providerToken);
        return BaseResponseDto.success(GlobalSuccessCode.OK, responseData);
    }

    @PostMapping("/v1/auth/reissue")
    public ResponseEntity<RefreshJwtTokenResponse> reissueToken(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String refreshToken = extractToken(authorizationHeader);
        RefreshJwtTokenResponse newTokens = refreshTokenService.reissue(refreshToken);
        return ResponseEntity.ok(newTokens);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length());
        }
        throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
    }
}
