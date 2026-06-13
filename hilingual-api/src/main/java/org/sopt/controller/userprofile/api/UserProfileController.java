package org.sopt.controller.userprofile.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.v1.NicknameAvailableRes;
import org.sopt.controller.userprofile.dto.*;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.annotation.UserId;
import org.sopt.controller.userprofile.service.UserProfileService;
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

    /*
     * 회원가입(프로필 등록)
     */
    @PostMapping("/profile")
    public ResponseEntity<UserIdRes> saveUserProfile(
            @UserId Long userId,
            @RequestBody @NotNull UserProfileReq userProfileReq
    ) {
        userProfileService.save(userId, userProfileReq);
        return ResponseEntity.ok(new UserIdRes(userId));
    }

    /*
     * 닉네임 변경
     */
    @PatchMapping("/profile/nickname")
    public BaseResponseDto<UserNicknameRes> changeUserNickname(
            @UserId Long userId,
            @RequestBody @NotNull UserNicknameReq userNicknameReq
    ) {
        return userProfileService.changeUserNickname(userId, userNicknameReq);
    }

    /*
     * 프로필 이미지 변경
     */
    @PatchMapping("/mypage/profileImg")
    public ResponseEntity<Void> changeUserProfileImg(
            @UserId Long userId,
            @RequestBody UserProfileImgReq userProfileImgReq
    ) {
        return ResponseEntity.ok(userProfileService.changeUserProfileImg(userId, userProfileImgReq));
    }
}
