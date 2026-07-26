package org.sopt.controller.user.api.v1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.type.AlarmType;
import org.sopt.annotation.UserTimezone;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.user.dto.v1.*;
import org.sopt.controller.user.service.UserService;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.sopt.jwt.annotation.UserId;
import org.sopt.user.domain.User;
import org.sopt.user.type.NotificationTab;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueTokensRes> reissue(
            HttpServletRequest request
    ){
        String refreshToken = jwtTokenProvider.getJwtFromRequest(request);
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }

    // 홈
    @GetMapping("/home/info")
    public ResponseEntity<HomeUserProfileRes> getUserProfile(
            @UserId Long userId,
            @UserTimezone final UserZone userZone
    ) {
        User user = userService.getHomeUserInfo(userId, userZone.zoneId());

        return ResponseEntity.ok(
                HomeUserProfileRes.from(
                        user.getUserProfile(),
                        user.getNotifyStatus(),
                        s3Service.toPublicUrl(user.getUserProfile().getProfileImg())
                )
        );
    }

    // 마이페이지
    @GetMapping("/mypage/info")
    public ResponseEntity<UserDefaultInfoRes> getUserDefaultInfo(
            @UserId Long userId
    ) {
        return ResponseEntity.ok(userService.getUserDefaultInfo(userId));
    }

    // 유저의 현재 알림 설정 상태 확인 API
    @GetMapping("/mypage/noti")
    public ResponseEntity<NotiStatusRes> getUserNotiStatus(
            @UserId Long userId
    ){
        return ResponseEntity.ok(userService.getUserNotiSatus(userId));
    }

    // 알림 설정 ON/OFF API
    @PatchMapping("/mypage/noti")
    public ResponseEntity<NotiStatusRes> toggleAlarmStatus(
            @UserId Long userId,
            @RequestParam String notiType
    ) {
        AlarmType type = AlarmType.from(notiType);
        return ResponseEntity.ok(userService.toggleAlarmStatus(userId, String.valueOf(type)));
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

    // 알림 리스트 확인하기 API
    @GetMapping("/notifications")
    public ResponseEntity<List<? extends NotificationItemRes>> getNotifications(
            @UserId Long userId,
            @RequestParam(defaultValue = "FEED") String tab
    ) {
        NotificationTab t = NotificationTab.from(tab);

        if (t == NotificationTab.FEED) {
            return ResponseEntity.ok(userService.getFeedAlarms(userId));
        } else {
            return ResponseEntity.ok(userService.getNoticeAlarms(userId));
        }
    }

}