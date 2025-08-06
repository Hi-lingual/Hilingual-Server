package org.sopt.controller.userprofile.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.userprofile.dto.UserProfileRes;
import org.sopt.controller.userprofile.service.UserProfileService;
import org.sopt.exception.code.GlobalSuccessCode;
import org.sopt.jwt.auth.util.UserAuthenticationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/info")
    public ResponseEntity<UserProfileRes> getUserProfile() {
        Long userId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping("/profile")
    public ResponseEntity<?> saveUserProfile(
            @RequestBody UserProfileReq userProfileReq
    ) {
        Long userId = UserAuthenticationUtils.getCurrentUserId();
        userProfileService.save(userId, userProfileReq);
        return ResponseEntity.ok(GlobalSuccessCode.OK);
    }
}
