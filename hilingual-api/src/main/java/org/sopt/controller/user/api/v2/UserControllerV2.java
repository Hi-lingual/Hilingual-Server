package org.sopt.controller.user.api.v2;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.user.dto.v2.HomeUserProfileResV2;
import org.sopt.controller.user.service.UserService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.user.domain.User;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserControllerV2 {

    private final UserService userService;
    private final S3Service s3Service;

    // 홈
    @GetMapping("/home/info")
    public ResponseEntity<HomeUserProfileResV2> getUserProfile(
            @UserId Long userId,
            @UserTimezone final UserZone userZone
    ) {
        User user = userService.getHomeUserInfo(userId, userZone.zoneId());

        return ResponseEntity.ok(
                HomeUserProfileResV2.from(
                        user.getUserProfile(),
                        user.getNotifyStatus(),
                        s3Service.toPublicUrl(user.getUserProfile().getProfileImg())
                )
        );
    }
}
