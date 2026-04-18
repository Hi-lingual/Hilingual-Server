package org.sopt.controller.admin.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.service.AdminNoticeService;
import org.sopt.jwt.core.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStore;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminNoticeService adminNoticeService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateRes> create(
            @Valid @RequestBody NoticeCreateReq req
    ) {
        return ResponseEntity.ok(adminNoticeService.create(req));
    }

    @PostMapping("/notices/{noticeId}/delivery")
    public ResponseEntity<Void> deliver(
            @PathVariable Long noticeId
    ) {
        adminNoticeService.deliver(noticeId);
        return ResponseEntity.ok().build();
    }

    /**
     * 서버가 어드민 토큰을 생성해서 반환
     */
    @GetMapping("/issue/{userId}")
    public String issueAdminToken(@PathVariable String userId) {
        return jwtTokenProvider.generateAdminStaticToken(Long.parseLong(userId));
    }

}

