package org.sopt.controller.feed.service;

import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.controller.feed.dto.LikedDiaryListRes;
import org.sopt.controller.feed.dto.SharedDiaryListRes;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.likeddiary.domain.LikedDiary;
import org.sopt.likeddiary.facade.LikedDiaryFacade;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final BlockFacade blockFacade;
    private final FollowFacade followFacade;
    private final DiaryFacade diaryFacade;
    private final LikedDiaryFacade likedDiaryFacade;

    @Transactional(readOnly = true)
    public FeedProfileRes getFeedProfile(Long userId, Long targetUserId) {
        // 존재하는 유저인지 확인
        userFacade.getUserById(userId);

        // isMine 검사
        boolean isMine = userId.equals(targetUserId);

        // 유저 프로필 조회
        UserProfile userProfile = userProfileFacade.getProfileByUserId(targetUserId);

        if (isMine) {
            return FeedProfileRes.from(userProfile, true, null, null, null);
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
                isBlocked
        );
    }

    public SharedDiaryListRes getSharedDiaries(Long targetUserId){
        // 유저 프로필 조회
        UserProfile userProfile = userFacade.getUserById(targetUserId).getUserProfile();

        // 다이어리 목록 조회
        List<Map<String, Object>> diaryData = getPublicDiariesWithIsLiked(targetUserId);

        List<Diary> diaries = diaryData.stream()
                .map(data -> (Diary) data.get("diary"))
                .toList();

        List<Boolean> isLikedByUser = diaryData.stream()
                .map(data -> (Boolean) data.get("isLiked"))
                .toList();

        return SharedDiaryListRes.of(userProfile, diaries, isLikedByUser);
    }

    public LikedDiaryListRes getLikedDiaries(Long targetUserId) {
        List<LikedDiary> likedDiaries = likedDiaryFacade.findLikedDiariesWithDetailsByUserId(targetUserId);

        return LikedDiaryListRes.of(
                likedDiaries.stream()
                        .map(likedDiary -> {
                            Diary diary = likedDiary.getDiary();
                            UserProfile diaryWriterProfile = diary.getUser().getUserProfile();
                            boolean isMine = diaryWriterProfile.getUser().getId().equals(targetUserId);

                            LikedDiaryListRes.LikedDiaryDetail.Profile profile = LikedDiaryListRes.LikedDiaryDetail.Profile.of(diaryWriterProfile, isMine);
                            LikedDiaryListRes.LikedDiaryDetail.LikedDiary likedDiaryDto = LikedDiaryListRes.LikedDiaryDetail.LikedDiary.of(diary);

                            return LikedDiaryListRes.LikedDiaryDetail.of(profile, likedDiaryDto);
                        })
                        .toList()
        );
    }

    // TODO 사용위치 확인 후 Diary 쪽으로 옮기는 것도 고려해볼 것
    public List<Map<String, Object>> getPublicDiariesWithIsLiked(Long userId) {
        // 해당 userId에 대해 공개된 다이어리 목록 조회
        List<Diary> diaries = diaryFacade.getPublicDiaries(userId);

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
}
