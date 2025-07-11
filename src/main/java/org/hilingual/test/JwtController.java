package org.hilingual.test;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.token.api.service.JwtProvider;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.hilingual.domain.token.core.domain.RefreshToken;
import org.hilingual.domain.token.api.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/test/jwt")
@RequiredArgsConstructor
public class JwtController {
    private static final Logger log = LoggerFactory.getLogger(JwtController.class);
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/generate")
    public ResponseEntity<JwtTokenResponse> generateTestToken(@RequestParam Long userId) {
        log.info("[token] 토큰 생성 요청 들어옴");
        JwtTokenResponse jwtToken = jwtProvider.generateToken(userId);
        refreshTokenService.save(userId, jwtToken.getRefreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/validate-access")
    public ResponseEntity<String> validateAccessToken() {
        return ResponseEntity.ok("Access Token is valid and authentication successful!");
    }

    @GetMapping("/validate-refresh")
    public ResponseEntity<String> validateRefreshToken(@RequestParam String refreshToken) {
        try {
            jwtProvider.validateToken(refreshToken);
            Optional<RefreshToken> storedTokenInfo = refreshTokenService.find(refreshToken);

            if (storedTokenInfo.isPresent()) {
                return ResponseEntity.ok("Refresh Token is valid and found in Redis!");
            } else {
                return ResponseEntity.badRequest().body("Refresh Token is invalid, expired, or not found in Redis.");
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.badRequest().body("Refresh Token is invalid or expired! Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error validating refresh token: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred during refresh token validation.");
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueTokens(@RequestParam String refreshToken) {
        try {
            JwtTokenResponse newTokens = refreshTokenService.reissue(refreshToken);
            return ResponseEntity.ok(newTokens);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred during token reissue.");
        }
    }
}
