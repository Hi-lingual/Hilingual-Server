package org.sopt.controller.userprofile.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.jwt.annotation.UserId;
import org.sopt.userprofile.dto.UserProfileRes;
import org.sopt.controller.userprofile.service.UserProfileService;
import org.sopt.exception.code.GlobalSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/info")
    public ResponseEntity<UserProfileRes> getUserProfile(
            @UserId Long userId
    ) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping("/profile")
    public ResponseEntity<?> saveUserProfile(
            @UserId Long userId,
            @RequestBody UserProfileReq userProfileReq
    ) {
        userProfileService.save(userId, userProfileReq);
        return ResponseEntity.ok(GlobalSuccessCode.OK);
    }
}
