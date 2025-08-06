package org.sopt.controller.user.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.service.UserService;
import org.sopt.dto.BaseResponseDto;
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
}
