package org.sopt.controller.block.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;
import org.sopt.controller.block.exception.BlockApiErrorCode;
import org.sopt.controller.block.exception.CannotSelfBlockException;
import org.sopt.controller.block.exception.CannotSelfUnblockException;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final UserFacade userFacade;
    private final BlockFacade blockFacade;
    private final UserProfileFacade userProfileFacade;
    private final FollowFacade followFacade;
    private final EntityManager entityManager;

    @Transactional
    public Void blockUser(final Long blockerId, final Long blockedId) {
        // 자기 자신인지 확인
        if(blockerId.equals(blockedId)) {
            throw new CannotSelfBlockException(BlockApiErrorCode.CANNOT_SELF_BLOCK);
        }

        final long firstId = Math.min(blockerId, blockedId);
        final long secondId = Math.max(blockerId, blockedId);

        // 유저 및 유저 프로필 조회
        final User firstUser = userFacade.getUserByIdWithLock(firstId);
        final User secondUser = userFacade.getUserByIdWithLock(secondId);

        final User blocker = (firstId == blockerId) ? firstUser : secondUser;
        final User blocked = (firstId == blockedId) ? firstUser : secondUser;

        // 기존 팔로우 관계 확인
        FollowRelation relation = followFacade.findFollowRelation(blockerId, blockedId);

        // 차단 관계 생성 및 팔로우 삭제
        blockFacade.block(blocker, blocked);
        int deletedCount = followFacade.deleteFollowRelations(blockerId, blockedId);
/*
        // 영속성 컨텍스트 강제 동기화
        if (deletedCount > 0) {
            entityManager.flush();
            entityManager.clear();
        }*/

        // 유저 프로필 팔로워/팔로잉 카운트 업데이트
        updateFollowerAndFollowingCount(relation, blockerId, blockedId);

        return null;
    }

    private void updateFollowerAndFollowingCount(
            FollowRelation relation,
            Long blockerId,
            Long blockedId
    ) {
        if (relation.getIsFollowing()) { // blocker가 blocked를 팔로우하고 있던 경우
            userProfileFacade.decrementFollowingCountByUserId(blockerId);
            userProfileFacade.decrementFollowerCountByUserId(blockedId);
        }

        if (relation.getIsFollowed()) { // blocked가 blocker를 팔로우하고 있던 경우
            userProfileFacade.decrementFollowingCountByUserId(blockedId);
            userProfileFacade.decrementFollowerCountByUserId(blockerId);
        }
    }

    @Transactional
    public Void unblockUser(final Long blockerId, final Long unblockedId) {
        if(blockerId.equals(unblockedId)) {
            throw new CannotSelfUnblockException(BlockApiErrorCode.CANNOT_SELF_UNBLOCK);
        }

        final User blocker = userFacade.getUserById(blockerId);
        final User unblocked = userFacade.getUserById(unblockedId);

        blockFacade.unblock(blocker, unblocked);
        return null;
    }

    public List<UserProfileSummaryRes> getBlockedUserList(final Long userId) {
        // 유저 존재 여부 확인(없을 시 UserRetriever에서 not found 예외 처리)
        userFacade.getUserById(userId);

        // 정렬된 blockedUserId 리스트 가져오기
        final List<Long> orderedBlockedUserIds = blockFacade.getBlockedUserId(userId);

        // 정렬되지 않은 상태의 UserProfile 리스트 가져오기
        final List<UserProfile> profiles = userProfileFacade.getProfilesByUserIds(orderedBlockedUserIds);

        // ID 순서를 Map에 저장, O(1) 탐색 위함
        final Map<Long, Integer> orderMap = IntStream.range(0, orderedBlockedUserIds.size())
                .boxed()
                .collect(Collectors.toMap(orderedBlockedUserIds::get, i -> i));

        // orderMap을 사용하여 원래 순서대로 정렬
        final List<UserProfile> sortedProfiles = profiles.stream()
                .sorted(Comparator.comparingInt(profile -> orderMap.getOrDefault(profile.getUser().getId(), Integer.MAX_VALUE)))
                .toList();

        // 정렬된 프로필 리스트를 응답 객체로 변환
        return sortedProfiles.stream()
                .map(UserProfileSummaryRes::from)
                .toList();

    }
}
