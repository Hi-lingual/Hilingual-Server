package org.sopt.controller.user.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.dto.UserDefaultInfoRes;
import org.sopt.controller.user.service.UserService;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.annotation.UserId;
import org.sopt.controller.user.dto.HomeUserProfileRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public BaseResponseDto<NicknameAvailableRes> getUserProfile(
            @RequestParam(value = "nickname") String nickname
    ) {
        return userService.getNicknameAvailable(nickname);
    }

    /*
     * 홈
     */
    @GetMapping("/home/info")
    public ResponseEntity<HomeUserProfileRes> getUserProfile(
            @UserId Long userId
    ) {
        return ResponseEntity.ok(userService.getHomeUserInfo(userId));
    }

    /*
     * 마이페이지
     */
    @GetMapping("/mypage/info")
    public ResponseEntity<UserDefaultInfoRes> getUserDefaultInfo(
            @UserId Long userId
    ) {
        return ResponseEntity.ok(userService.getUserDefaultInfo(userId));
    }
}
