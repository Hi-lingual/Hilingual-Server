package org.sopt.controller.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.token.TokenService;
import org.sopt.controller.user.dto.NotiStatusRes;
import org.sopt.controller.user.dto.UserDefaultInfoRes;
import org.sopt.controller.user.exception.CannotLoadProviderException;
import org.sopt.controller.user.exception.UserApiErrorCode;
import org.sopt.feedalarm.facade.FeedAlarmFacade;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.sopt.user.domain.User;
import org.sopt.controller.user.dto.HomeUserProfileRes;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.controller.user.dto.NoticeDetailRes;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.sopt.noticedelivery.facade.NoticeDeliveryFacade;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final TokenService tokenService;
    private final UserFacade userFacade;
    private final NoticeDeliveryFacade noticeDeliveryFacade;
    private final FeedAlarmFacade feedAlarmFacade;

    public UserDefaultInfoRes getUserDefaultInfo(final long userId) {
        User user = userFacade.getUserById(userId);

        return UserDefaultInfoRes.from(
                user.getUserProfile(),
                parseProviderInfo(user.getProvider())
        );
    }

    public HomeUserProfileRes getHomeUserInfo(final long userId) {
        User user = userFacade.getUserById(userId);

        return HomeUserProfileRes.from(
                user.getUserProfile(),
                user.getNotifyStatus()
        );
    }

    @Transactional
    public ReissueTokensRes reissue(final String refreshToken) {
        return tokenService.reissue(refreshToken);
    }

    private String parseProviderInfo(String provider) {
        String loginProviderInfo;

        switch (provider) {
            case "GOOGLE":
                loginProviderInfo = "구글 로그인";
                break;
            case "APPLE":
                loginProviderInfo = "애플 로그인";
                break;
            default:
                throw new CannotLoadProviderException(UserApiErrorCode.PROVIDER_LOAD_ERROR);
        }
        return loginProviderInfo;
    }

    public NotiStatusRes getUserNotiSatus(final Long userId) {
        Map<AlarmType, Boolean> map = alarmPreferenceFacade.getAlarmStatusMap(userId);

        boolean marketing = Boolean.TRUE.equals(map.get(AlarmType.MARKETING));
        boolean feed = Boolean.TRUE.equals(map.get(AlarmType.FEED));

        return new NotiStatusRes(marketing, feed);
    }

    @Transactional
    public NotiStatusRes toggleAlarmStatus(final Long userId, final String type) {
        alarmPreferenceFacade.toggle(userId, AlarmType.from(type));
        return getUserNotiSatus(userId);
    }

    @Transactional
    public NoticeDetailRes getNotificationDetail(final long userId, final long noticeId) {
        NoticeDelivery delivery = noticeDeliveryFacade
                .findByUserIdAndNoticeIdWithDetail(userId, noticeId);

        delivery.markReadIfNeeded(LocalDateTime.now());

        return NoticeDetailRes.from(
                delivery.getNotice(),
                delivery.getNotice().getNoticeDetail()
        );

    }

    @Transactional
    public void markNoticeRead(final long userId, final long noticeId){
        feedAlarmFacade.markAlarmAsRead(userId, noticeId);
        return;
    }

}
