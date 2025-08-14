package org.sopt.api.test;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.controller.test.dto.jwt.*;
import org.sopt.jwt.annotation.UserId;
import org.sopt.jwt.auth.JwtTokenProvider;
import org.sopt.jwt.auth.authentication.UserAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰 발급 테스트 (멀티 디바이스/세션 검증용)
     */
    @PostMapping("/token/issue")
    public ResponseEntity<JwtTokensDto> issueToken(
            @Valid @RequestBody IssueTokenRequest req
    ) {
        final String sessionId = jwtTokenProvider.newSessionId();
        log.info("발급된 SessionId = {}", sessionId);

        final String accessToken  = jwtTokenProvider.generateAccessToken(
                req.userId(), req.role(), req.provider(), sessionId
        );
        final String refreshToken = jwtTokenProvider.generateRefreshToken(
                req.userId(), req.provider(), sessionId
        );

        JwtTokensDto tokens = JwtTokensDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(tokens);
    }

    /**
     * 인증만 필요 (USER/ADMIN 모두 OK)
     */
    @GetMapping("/me")
    public ResponseEntity<WhoAmI> me(
            @UserId Long userId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                WhoAmI.builder()
                        .userId(userId)
                        .role(extractRole(authentication))
                        .build()
        );
    }

    /**
     * USER, ADMIN 둘 다 OK
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/user-only")
    public ResponseEntity<String> userOnly() {
        return ResponseEntity.ok("USER or ADMIN 접근 성공");
    }

    /**
     * ADMIN 전용
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("ADMIN 접근 성공");
    }

    /**
     * content + @UserId 사용 테스트
     */
    @PostMapping("/security")
    public ResponseEntity<TestDto> testSecurity(
            @UserId Long userId,
            @Valid @RequestBody TestSecurity body
    ) {
        return ResponseEntity.ok(TestDto.builder()
                .content(body.name() + " " + userId)
                .build());
    }

    private String extractRole(Authentication authentication) {
        if (authentication instanceof UserAuthentication ua) {
            return ua.getRole().name();
        }
        return "UNKNOWN";
    }

}