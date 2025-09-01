package org.sopt.controller.authcode.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.authcode.dto.AuthCodeReq;
import org.sopt.controller.authcode.service.AuthCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthCodeController {

    private final AuthCodeService authCodeService;

    @PostMapping("/api/v1/auth/verify")
    public ResponseEntity<Void> verifyAuthCode(
            @RequestBody AuthCodeReq req
    ) {
        return ResponseEntity.ok(authCodeService.verifyAuthCode(req.code()));
    }
}
