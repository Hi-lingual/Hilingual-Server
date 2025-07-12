package org.hilingual.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.auth.api.dto.req.AuthRequest;
import org.hilingual.auth.api.service.AuthService;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/v1/auth/login")
    public BaseResponseDto<JwtTokenResponse> socialLogin(
            @RequestHeader("Provider-Token") String providerToken,
            @RequestBody AuthRequest authRequest
    ) {
        JwtTokenResponse responseData = authService.googleLogin(authRequest.provider(), providerToken);
        return BaseResponseDto.success(GlobalSuccessCode.OK, responseData);
    }
}
