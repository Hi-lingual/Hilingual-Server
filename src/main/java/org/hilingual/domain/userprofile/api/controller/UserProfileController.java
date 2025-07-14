package org.hilingual.domain.userprofile.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.auth.util.UserAuthenticationUtils;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.hilingual.domain.userprofile.api.dto.req.UserProfileRequest;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.api.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.hilingual.auth.util.UserAuthenticationUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/info")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        Long userId = getCurrentUserId();
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
}
