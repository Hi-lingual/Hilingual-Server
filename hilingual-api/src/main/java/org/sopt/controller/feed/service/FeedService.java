package org.sopt.controller.feed.service;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.facade.AlarmPreferenceFacade;
import org.sopt.alarmpreference.type.AlarmType;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.feed.dto.*;
import org.sopt.controller.feed.dto.DiaryWriterProfileRes;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.controller.feed.dto.LikedDiaryListRes;
import org.sopt.controller.feed.dto.SharedDiaryListRes;
import org.sopt.controller.feed.dto.UserListRes;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.feed.dto.FeedProjection;
import org.sopt.feed.facade.FeedFacade;
import org.sopt.feedalarm.facade.FeedAlarmFacade;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.likeddiary.domain.LikedDiary;
import org.sopt.likeddiary.facade.LikedDiaryFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.dto.UserSearchDto;
import org.sopt.controller.notification.service.FcmNotificationService;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final BlockFacade blockFacade;
    private final FollowFacade followFacade;
    private final DiaryFacade diaryFacade;
    private final LikedDiaryFacade likedDiaryFacade;
    private final FeedFacade feedFacade;
    private final S3Service s3Service;
    private final FeedAlarmFacade feedAlarmFacade;
    private final AlarmPreferenceFacade alarmPreferenceFacade;
    private final FcmNotificationService fcmNotificationService;

    @Transactional(readOnly = true)
    public FeedProfileRes getFeedProfile(Long userId, Long targetUserId) {
        // 존재하는 유저인지 확인
        userFacade.getUserById(userId);

        UserProfile userProfile = userProfileFacade.getProfileByUserId(targetUserId);

        boolean isMine = userId.equals(targetUserId);
        if (isMine) {
            return FeedProfileRes.from(
                    userProfile,
                    true,
                    null,
                    null,
                    null,
                    s3Service.toPublicUrl(userProfile.getProfileImg())
            );
        }

        // 내 프로필이 아닌 경우
        // 팔로우 관계 검사
        FollowRelation followRelation = followFacade.findFollowRelation(userId, targetUserId);

        // 차단 여부 검사
        boolean isBlocked = blockFacade.existsByBlockerIdAndBlockedId(userId, targetUserId);

        return FeedProfileRes.from(
                userProfile,
                false,
                followRelation.getIsFollowing(),
                followRelation.getIsFollowed(),
                isBlocked,
                s3Service.toPublicUrl(userProfile.getProfileImg())
        );
    }

    public SharedDiaryListRes getSharedDiaries(Long userId, Long targetUserId) {
        // 유저 프로필 조회
        UserProfile userProfile = userFacade.getUserById(targetUserId).getUserProfile();

        // 프로필 이미지 URL 변환
        String profileImgUrl = null;
        if (userProfile.getProfileImg() != null) {
            profileImgUrl = s3Service.toPublicUrl(userProfile.getProfileImg());
        }

        // 다이어리 목록 조회
        List<Map<String, Object>> diaryData = getPublicDiariesWithIsLiked(userId, targetUserId);

        List<Diary> diaries = diaryData.stream()
                .map(data -> (Diary) data.get("diary"))
                .toList();

        List<Boolean> isLikedByUser = diaryData.stream()
                .map(data -> (Boolean) data.get("isLiked"))
                .toList();

        // 다이어리 이미지 URL 변환 및 DTO 생성
        List<SharedDiaryListRes.SharedDiary> sharedDiaries = IntStream.range(0, diaries.size())
                .mapToObj(i -> {
                    Diary diary = diaries.get(i);
                    String diaryImgUrl = null;
                    if (diary.getImageUrl() != null) {
                        diaryImgUrl = s3Service.toPublicUrl(diary.getImageUrl());
                    }
                    return SharedDiaryListRes.SharedDiary.of(diary, isLikedByUser.get(i), diaryImgUrl);
                })
                .toList();

        return SharedDiaryListRes.of(userProfile, profileImgUrl, sharedDiaries);
    }

    public LikedDiaryListRes getLikedDiaries(Long targetUserId) {
        List<LikedDiary> likedDiaries = likedDiaryFacade.getLikedDiariesWithAllDetails(targetUserId);

        return LikedDiaryListRes.of(
                likedDiaries.stream()
                        .map(likedDiary -> {
                            Diary diary = likedDiary.getDiary();
                            UserProfile diaryWriterProfile = diary.getUser().getUserProfile();

                            // 프로필 이미지 URL 변환
                            String profileImgUrl = diaryWriterProfile.getProfileImg();
                            if (profileImgUrl != null) {
                                profileImgUrl = s3Service.toPublicUrl(profileImgUrl);
                            }

                            // 일기 이미지 URL 변환
                            String diaryImgUrl = diary.getImageUrl();
                            if (diaryImgUrl != null){
                                diaryImgUrl = s3Service.toPublicUrl(diaryImgUrl);
                            }

                            boolean isMine = diaryWriterProfile.getUser().getId().equals(targetUserId);

                            LikedDiaryListRes.LikedDiaryDetail.Profile profile = LikedDiaryListRes.LikedDiaryDetail.Profile.of(diaryWriterProfile, isMine, profileImgUrl);
                            LikedDiaryListRes.LikedDiaryDetail.LikedDiary likedDiaryDto = LikedDiaryListRes.LikedDiaryDetail.LikedDiary.of(diary, diaryImgUrl);

                            return LikedDiaryListRes.LikedDiaryDetail.of(profile, likedDiaryDto);
                        })
                        .toList()
        );
    }

    public DiaryWriterProfileRes getDiaryWriterProfile(Long userId, Long diaryId) {
        Diary diary = diaryFacade.getDiaryWithDetails(diaryId);
        final boolean isMine = diary.getUser().getId().equals(userId);
        final boolean isLiked = likedDiaryFacade.findUserAndDiaryExist(userId, diaryId);

        String profileImgUrl = diary.getUser().getUserProfile().getProfileImg();
        if (profileImgUrl!= null) {
            profileImgUrl = s3Service.toPublicUrl(profileImgUrl);
        }
        return DiaryWriterProfileRes.of(diary, isMine, isLiked, profileImgUrl);
    }

    public UserListRes getUserList(Long userId, String keyword) {
        String likeKeyword = "%" + keyword + "%";
        String startKeyword = keyword + "%";

        List<UserSearchDto> userList = userProfileFacade.getUserListByNickname(userId, likeKeyword, startKeyword);

        List<UserListRes.SearchUser> searchUserList = userList.stream()
                .map(projection -> {
                    String originalImgUrl = projection.profileImg();

                    String publicImgUrl = (originalImgUrl != null && !originalImgUrl.trim().isEmpty())
                            ? s3Service.toPublicUrl(originalImgUrl)
                            : " ";

                    return new UserListRes.SearchUser(
                            projection.userId(),
                            publicImgUrl,
                            projection.nickname(),
                            projection.isFollowing(),
                            projection.isFollowed()
                    );
                })
                .toList();

        return new UserListRes(searchUserList);
    }

    public RecommendFeedListRes getRecommendFeeds(final long userId, final int page, final int size) {
        Pageable pageable =  PageRequest.of(page, size);

        List<FeedProjection> projections = feedFacade.findRecommendFeeds(userId, pageable);

        LocalDateTime now = LocalDateTime.now();
        List<RecommendFeedListRes.RecommendFeed> recommendFeeds = projections.stream()
                .map(projection -> {
                    // S3Service를 활용해 URL 변환
                    String profileImageUrl = s3Service.toPublicUrl(projection.getProfileImg());
                    String diaryImageUrl = s3Service.toPublicUrl(projection.getDiaryImg());

                    // Projection 데이터로 DTO 생성
                    RecommendFeedListRes.RecommendFeedProfile profile = new RecommendFeedListRes.RecommendFeedProfile(
                            projection.getUserId(),
                            projection.getIsMine(),
                            profileImageUrl,
                            projection.getNickname(),
                            projection.getStreak()
                    );

                    LocalDateTime sharedTime = projection.getSharedDate();
                    long minutesDiff = (sharedTime != null) ? Duration.between(sharedTime, now).toMinutes() : 0L;

                    RecommendFeedListRes.RecommendFeedDiary diary = new RecommendFeedListRes.RecommendFeedDiary(
                            projection.getDiaryId(),
                            (minutesDiff < 1) ? 0L : minutesDiff,
                            projection.getLikeCount(),
                            projection.getIsLiked(),
                            diaryImageUrl,
                            projection.getOriginalText()
                    );

                    return new RecommendFeedListRes.RecommendFeed(profile, diary);
                })
                .collect(Collectors.toList());

        return new RecommendFeedListRes(recommendFeeds);
    }

    public FollowFeedListRes getFollowFeeds(final long userId, final int page, final int size) {
        // 현재 유저가 팔로우하는 유저 존재 여부 확인
        boolean hasFollowing = followFacade.haveFollowing(userId);

        // 현재 유저가 팔로우하는 유저가 존재하지 않는 경우
        if(!hasFollowing) {
            return FollowFeedListRes.empty();
        }

        // 현재 유저가 팔로우하는 유저가 존재하는 경우
        Pageable pageable =  PageRequest.of(page, size);
        List<FeedProjection> projections = feedFacade.findFollowingFeeds(userId, pageable);

        LocalDateTime now = LocalDateTime.now();
        List<FollowFeedListRes.FollowFeed> followFeeds = projections.stream()
                .map(projection -> {
                    // S3Service를 활용해 URL 변환
                    String profileImageUrl = s3Service.toPublicUrl(projection.getProfileImg());
                    String diaryImageUrl = s3Service.toPublicUrl(projection.getDiaryImg());

                    // Projection 데이터로 DTO 생성
                    FollowFeedListRes.FollowFeedProfile profile = new FollowFeedListRes.FollowFeedProfile(
                            projection.getUserId(),
                            projection.getIsMine(),
                            profileImageUrl,
                            projection.getNickname(),
                            projection.getStreak()
                    );

                    LocalDateTime sharedTime = projection.getSharedDate();
                    long minutesDiff = (sharedTime != null) ? Duration.between(sharedTime, now).toMinutes() : 0L;

                    FollowFeedListRes.FollowFeedDiary diary = new FollowFeedListRes.FollowFeedDiary(
                            projection.getDiaryId(),
                            (minutesDiff < 1) ? 0L : minutesDiff,
                            projection.getLikeCount(),
                            projection.getIsLiked(),
                            diaryImageUrl,
                            projection.getOriginalText()
                    );

                    return new FollowFeedListRes.FollowFeed(profile, diary);
                })
                .collect(Collectors.toList());

        return new FollowFeedListRes(followFeeds, true);
    }

    private List<Map<String, Object>> getPublicDiariesWithIsLiked(Long userId, Long targetUserId) {
        // 해당 userId에 대해 공개된 다이어리 목록 조회
        List<Diary> diaries = diaryFacade.getPublicDiaries(targetUserId);

        // 다이어리 ID 목록 추출
        List<Long> diaryIds = diaries.stream().map(Diary::getId).toList();

        // 좋아요 누른 다이어리 ID
        Set<Long> likedDiaryIds = new HashSet<>(likedDiaryFacade.findLikedDiaryIdsByUserIdAndDiaryIdsIn(userId, diaryIds));

        // 다이어리 목록 순서 유지 + 좋아요 매핑
        return diaries.stream()
                .map(diary -> Map.of(
                        "diary", diary,
                        "isLiked", likedDiaryIds.contains(diary.getId())
                ))
                .toList();
    }

    @Transactional
    public LikeToggleRes toggleLike(Long userId, Long diaryId, boolean wantLike) {
        diaryFacade.validateReadable(userId, diaryId);

        User user  = userFacade.getUserById(userId);
        Diary diary = diaryFacade.getDiaryById(diaryId);

        boolean alreadyLiked = likedDiaryFacade.findUserAndDiaryExist(user.getId(), diary.getId());

        if (wantLike && !alreadyLiked) {
            likedDiaryFacade.like(user, diary);
            diaryFacade.increaseLikeCount(diaryId);

            // FEED 알림 허용 사용자만 알림 생성
            if (alarmPreferenceFacade.isEnabled(diary.getUser().getId(), AlarmType.FEED)) {
                // 같은 다이어리/같은 액터의 중복 알림 방지 로직은 Facade 내부에서 처리
                feedAlarmFacade.createLikeDiaryAlarm(diary, user);
                String dateStr = diary.getWrittenDate().format(DateTimeFormatter.ofPattern("M월 d일"));
                String body = user.getUserProfile().getNickname() + "님이 당신의 " + dateStr + " 일기에 공감했습니다.";
                fcmNotificationService.sendLikeNotification(diary.getUser().getId(), diary.getId(), body);
            }
            return LikeToggleRes.of(true);
        }

        if (!wantLike && alreadyLiked) {
            likedDiaryFacade.unlike(user.getId(), diary.getId());
            diaryFacade.decreaseLikeCount(diaryId);
            return LikeToggleRes.of(false);
        }
        return LikeToggleRes.of(alreadyLiked);
    }

}
