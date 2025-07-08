package org.hilingual.test;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.security.jwt.JwtProvider;
import org.hilingual.domain.security.jwt.JwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final JwtProvider jwtProvider;

    @GetMapping("/generate")
    public ResponseEntity<JwtToken> generateTestToken(@RequestParam Long userId) {
        JwtToken jwtToken = jwtProvider.generateToken(userId);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/validate-access")
    public ResponseEntity<String> validateAccessToken() {
        return ResponseEntity.ok("Access Token is valid and authentication successful!");
    }

    @GetMapping("/validate-refresh")
    public ResponseEntity<String> validateRefreshToken(@RequestParam String refreshToken) {
        boolean isValid = jwtProvider.validateToken(refreshToken);
        if (isValid) {
            return ResponseEntity.ok("Refresh Token is valid!");
        } else {
            return ResponseEntity.badRequest().body("Refresh Token is invalid or expired!");
        }
    }
}
