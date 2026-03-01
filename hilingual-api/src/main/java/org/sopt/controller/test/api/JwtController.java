package org.sopt.controller.test.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.controller.test.dto.jwt.*;
import org.sopt.controller.test.dto.jwt.JwtTokensDto;
import org.sopt.jwt.annotation.UserId;
import org.sopt.jwt.auth.authentication.UserAuthentication;
import org.sopt.jwt.auth.domain.Token;
import org.sopt.jwt.auth.domain.TokenRepository;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.core.TokenHasher;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/test/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final UserFacade userFacade;
    private final TokenHasher tokenHasher;

    /**
     * 토큰 발급 테스트 (멀티 디바이스/세션 검증용)
     */
    @PostMapping("/token/issue")
    public ResponseEntity<JwtTokensDto> issueToken(
            @Valid @RequestBody IssueTokenRequest req
    ) {
        // userId에 해당하는 유저가 실제로 db에 있는지 찾기
        User user = userFacade.getUserById(req.userId());

        /* TODO [Soft Migration]
            구버전 앱 호환성을 위해 당분간 유지하며, 신버전 앱에서는 null로 들어옴
            기존 앱 Version 사용 유저가 없는 경우 완전 삭제 요망
        */
        // 신버전 vs 구버전 분기 처리를 위한 식별자 결정
        String identifier;
        boolean isNewVersion = StringUtils.hasText(req.uuid());

        if (isNewVersion) {
            identifier = req.uuid();
            log.info("신버전 앱 토큰 발급. DeviceUuid = {}", identifier);
        } else {
            identifier = jwtTokenProvider.newSessionId();
            log.info("구버전 앱 토큰 발급. 발급된 랜덤 SessionId = {}", identifier);
        }

        final String accessToken  = jwtTokenProvider.generateAccessToken(
                req.userId(), req.role(), req.provider(), identifier
        );
        final String refreshToken = jwtTokenProvider.generateRefreshToken(
                req.userId(), req.provider(), identifier
        );

        Token token;
        if (isNewVersion) {
            token = Token.create(
                    user.getId(),
                    req.provider(),
                    tokenHasher.hash(refreshToken),
                    identifier
            );
        } else {
            token = Token.createLegacy(
                    user.getId(),
                    req.provider(),
                    tokenHasher.hash(refreshToken),
                    req.deviceName(),
                    req.deviceType(),
                    req.osType(),
                    req.osVersion(),
                    req.appVersion()
            );
        }

        tokenRepository.save(token);

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