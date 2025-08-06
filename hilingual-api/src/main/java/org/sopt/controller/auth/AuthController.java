package org.sopt.controller.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.auth.service.AuthService;
import org.sopt.dto.AuthRequest;
import org.sopt.dto.BaseResponseDto;
import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.exception.code.GlobalSuccessCode;
import org.sopt.jwt.auth.RefreshTokenService;
import org.sopt.jwt.dto.res.JwtTokenResponse;
import org.sopt.jwt.dto.res.RefreshJwtTokenResponse;
import org.sopt.jwt.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
