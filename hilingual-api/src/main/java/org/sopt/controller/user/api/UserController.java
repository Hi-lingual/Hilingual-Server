package org.sopt.controller.user.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.dto.NoticeDetailRes;
import org.sopt.controller.user.dto.UserDefaultInfoRes;
import org.sopt.controller.user.service.UserService;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.springframework.http.ResponseEntity;
import org.sopt.jwt.annotation.UserId;
import org.sopt.controller.user.dto.HomeUserProfileRes;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueTokensRes> reissue(
            HttpServletRequest request
    ){
        String refreshToken = jwtTokenProvider.getJwtFromRequest(request);
        return ResponseEntity.ok(userService.reissue(refreshToken));
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

    // 공지 사항 알림 상세보기 API
    @GetMapping("/notifications/{noticeId}")
    public ResponseEntity<NoticeDetailRes> getNotificationDetail(
            @UserId Long userId,
            @PathVariable Long noticeId
    ) {
        return ResponseEntity.ok(userService.getNotificationDetail(userId, noticeId));
    }

    // 알림 읽음 처리 API
    @PatchMapping("/notifications/{noticeId}/read")
    public ResponseEntity<Void> markNoticeRead(
            @UserId Long userId,
            @PathVariable Long noticeId
    ) {
        userService.markNoticeRead(userId, noticeId);
        return ResponseEntity.ok().build();
    }
}
