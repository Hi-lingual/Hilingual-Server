package org.hilingual.domain.userprofile.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.api.service.UserProfileService;
import org.springframework.http.ResponseEntity;
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
}
