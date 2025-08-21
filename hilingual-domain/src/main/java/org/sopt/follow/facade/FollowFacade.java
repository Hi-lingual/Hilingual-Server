package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.domain.Follow;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.sopt.follow.exception.FollowAlreadyExistsException;
import org.sopt.follow.exception.FollowCoreErrorCode;
import org.sopt.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sopt.follow.domain.FollowTableConstants.UK_FOLLOWER_FOLLOWEE;

@Component
@RequiredArgsConstructor
public class FollowFacade {

    private final FollowRetriever followRetriever;
    private final FollowSaver followSaver;
    private final FollowRemover followRemover;

    // 이미 팔로우 중이면 예외, 아니면 통과
    @Transactional(readOnly = true)
    public void assertNotFollowing(User follower, User followee) {
        followRetriever.assertNotFollowing(follower, followee);
    }

    //팔로 우 생성
    @Transactional
    public Long save(User follower, User followee) {
        try {
            Follow saved = followSaver.save(Follow.create(follower, followee));
            return saved.getFollowId();
        } catch (DataIntegrityViolationException e) {
            if (isUniqueViolation(e, UK_FOLLOWER_FOLLOWEE)) {
                throw new FollowAlreadyExistsException(FollowCoreErrorCode.FOLLOW_ALREADY_EXISTS);
            }
            throw e;
        }
    }

    // 언팔로우
    @Transactional
    public void deleteIfExists(User follower, User followee) {
        followRemover.deleteByFollowerIdAndFolloweeId(follower.getId(), followee.getId());
    }

    // a -> b 팔로우 여부 확인
    @Transactional(readOnly = true)
    public boolean isFollowing(User a, User b) {
        return followRetriever.existsByFollowerIdAndFolloweeId(a.getId(), b.getId());
    }

    // 나를 팔로우하는 사람들 + 내가 그들을 팔로우 중인지
    @Transactional(readOnly = true)
    public List<FollowerIdAndIsFollowing> getFollowerListAndIsFollowing(Long userId) {
        return followRetriever.getFollowerListAndIsFollowing(userId);
    }

    // 내가 팔로우하는 사람들 + 그들이 나를 팔로우 중인지
    @Transactional(readOnly = true)
    public List<FolloweeIdAndIsFollowed> getFolloweeListAndIsFollowed(Long userId) {
        return followRetriever.getFolloweeListAndIsFollowedByUserId(userId);
    }

    private boolean isUniqueViolation(DataIntegrityViolationException e, String constraintName) {
        Throwable cause = e.getMostSpecificCause();
        String msg = (cause != null ? cause.getMessage() : "");
        return msg != null && msg.contains(constraintName);
    }
}