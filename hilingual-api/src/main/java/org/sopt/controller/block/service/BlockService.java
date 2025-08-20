package org.sopt.controller.block.service;

import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;
import org.sopt.controller.block.exception.BlockApiErrorCode;
import org.sopt.controller.block.exception.CannotSelfBlockException;
import org.sopt.controller.block.exception.CannotSelfUnblockException;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
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

    @Transactional
    public Void blockUser(Long blockerId, Long blockedId) {
        // 자기 자신인지 확인
        if(blockerId.equals(blockedId)) {
            throw new CannotSelfBlockException(BlockApiErrorCode.CANNOT_SELF_BLOCK);
        }

        Long firstId = Math.min(blockerId, blockedId);
        long secondId = Math.max(blockerId, blockedId);

        User firstUser = userFacade.getUserByIdWithLock(firstId);
        User secondUser = userFacade.getUserByIdWithLock(secondId);

        User blocker = (firstId.equals(blockerId)) ? firstUser : secondUser;
        User blocked = (firstId.equals(blockedId)) ? firstUser : secondUser;

        blockFacade.block(blocker, blocked);
        return null;
    }

    @Transactional
    public Void unblockUser(Long blockerId, Long unblockedId) {
        if(blockerId.equals(unblockedId)) {
            throw new CannotSelfUnblockException(BlockApiErrorCode.CANNOT_SELF_UNBLOCK);
        }

        User blocker = userFacade.getUserById(blockerId);
        User unblocked = userFacade.getUserById(unblockedId);

        blockFacade.unblock(blocker, unblocked);
        return null;
    }

    public List<UserProfileSummaryRes> getBlockedUserList(Long userId) {
        // 유저 존재 여부 확인(없을 시 UserRetriever에서 not found 예외 처리)
        userFacade.getUserById(userId);

        // 정렬된 blockedUserId 리스트 가져오기
        List<Long> orderedBlockedUserIds = blockFacade.getBlockedUserId(userId);

        // 정렬되지 않은 상태의 UserProfile 리스트 가져오기
        List<UserProfile> profiles = userProfileFacade.getProfilesByUserIds(orderedBlockedUserIds);

        // ID 순서를 Map에 저장, O(1) 탐색 위함
        Map<Long, Integer> orderMap = IntStream.range(0, orderedBlockedUserIds.size())
                .boxed()
                .collect(Collectors.toMap(orderedBlockedUserIds::get, i -> i));

        // orderMap을 사용하여 원래 순서대로 정렬
        List<UserProfile> sortedProfiles = profiles.stream()
                .sorted(Comparator.comparingInt(profile -> orderMap.getOrDefault(profile.getUser().getId(), Integer.MAX_VALUE)))
                .toList();

        // 정렬된 프로필 리스트를 응답 객체로 변환
        return sortedProfiles.stream()
                .map(UserProfileSummaryRes::from)
                .toList();

    }
}
