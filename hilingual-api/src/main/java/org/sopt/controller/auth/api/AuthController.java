package org.sopt.controller.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.auth.dto.SocialLoginReq;
import org.sopt.controller.auth.dto.SocialLoginRes;
import org.sopt.controller.auth.service.AuthService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.jwt.core.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 소셜 로그인(구글, 애플)
     */
    @PostMapping("/login")
    public ResponseEntity<SocialLoginRes> socialLogin(
            @RequestHeader("Provider-Token") String providerToken,
            @Valid @RequestBody SocialLoginReq req
    ) {
        return ResponseEntity.ok(authService.socialLogin(providerToken, req));
    }

    /*
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJwtFromRequest(request);
        return ResponseEntity.ok(authService.logout(accessToken));
    }
}
