package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.sopt.follow.exception.FollowAlreadyExistsException;
import org.sopt.follow.exception.FollowCoreErrorCode;
import org.sopt.follow.repository.FollowRepository;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowRetriever {

    private final FollowRepository followRepository;

    /** 이미 팔로우 중이면 예외 */
    @Transactional(readOnly = true)
    public void assertNotFollowing(User follower, User followee) {
        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new FollowAlreadyExistsException(FollowCoreErrorCode.FOLLOW_ALREADY_EXISTS);
        }
    }

    /** 팔로우하는 상대가 1명이라도 존재하는지 */
    @Transactional(readOnly = true)
    public boolean existsByFollowerId(Long followerId) {
        return followRepository.existsByFollowerId(followerId);
    }

    /** (a -> b) 팔로우 존재 여부 */
    @Transactional(readOnly = true)
    public boolean existsByFollowerIdAndFolloweeId(Long aId, Long bId) {
        return followRepository.existsByFollowerIdAndFolloweeId(aId, bId);
    }

    /** 나를 팔로우하는 사람들 + 내가 그들을 팔로우 중인지 */
    @Transactional(readOnly = true)
    public List<FollowerIdAndIsFollowing> getFollowerListAndIsFollowing(Long userId) {
        return followRepository.findFollowerAndIsFollowingByUserId(userId);
    }

    /** 내가 팔로우하는 사람들 + 그들이 나를 팔로우 중인지 */
    @Transactional(readOnly = true)
    public List<FolloweeIdAndIsFollowed> getFolloweeListAndIsFollowed(Long userId) {
        return followRepository.findFolloweeAndIsFollowedByUserId(userId);
    }

    public FollowRelation findFollowRelation(Long userId, Long targetUserId) {
        return followRepository.findFollowRelation(userId, targetUserId);
    }
}