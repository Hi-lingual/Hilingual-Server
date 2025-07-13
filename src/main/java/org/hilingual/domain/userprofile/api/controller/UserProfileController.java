package org.hilingual.domain.userprofile.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.hilingual.domain.userprofile.api.dto.req.UserProfileRequest;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.api.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/userprofile")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        Long userId = 1L; // TODO: 인증 연동 후 교체
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping("/profile")
    public ResponseEntity<?> saveUserProfile(
            @RequestBody UserProfileRequest userProfileRequest
    ) {
        Long userId = getCurrentUserId();
        userProfileService.save(userId, userProfileRequest);
        return ResponseEntity.ok(GlobalSuccessCode.OK);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
        return (Long) authentication.getPrincipal();
    }
}
