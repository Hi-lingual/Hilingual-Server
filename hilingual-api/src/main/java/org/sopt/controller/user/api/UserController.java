package org.sopt.controller.user.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.service.UserService;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/profile")
    public BaseResponseDto<NicknameAvailableRes> getUserProfile(
            @RequestParam(value = "nickname") String nickname
    ) {
        return userService.getNicknameAvailable(nickname);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueTokensRes> reissue(
            HttpServletRequest request
    ){
        String refreshToken = jwtTokenProvider.getJwtFromRequest(request);
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }
}
