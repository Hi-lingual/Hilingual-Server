package org.hilingual.domain.user.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.domain.user.api.dto.res.NicknameAvailableResponse;
import org.hilingual.domain.user.api.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/profile")
    public BaseResponseDto<NicknameAvailableResponse> getUserProfile(
            @RequestParam(value = "nickname") String nickname
    ) {
        return userService.getNicknameAvailable(nickname);
    }
}
