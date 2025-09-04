package org.sopt.controller.userprofile.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.userprofile.dto.UserProfileImgReq;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.annotation.UserId;
import org.sopt.controller.userprofile.service.UserProfileService;
import org.sopt.exception.code.GlobalSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile/check")
    public BaseResponseDto<NicknameAvailableRes> getUserProfile(
            @RequestParam(value = "nickname") String nickname
    ) {
        return userProfileService.getNicknameAvailable(nickname);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> saveUserProfile(
            @UserId Long userId,
            @RequestBody @NotNull UserProfileReq userProfileReq
    ) {
        userProfileService.save(userId, userProfileReq);
        return ResponseEntity.ok(GlobalSuccessCode.OK);
    }

    @PatchMapping("/mypage/profileImg")
    public ResponseEntity<Void> changeUserProfileImg(
            @UserId Long userId,
            @RequestBody @NotNull UserProfileImgReq userProfileImgReq
    ) {
        return ResponseEntity.ok(userProfileService.changeUserProfileImg(userId, userProfileImgReq));
    }
}
