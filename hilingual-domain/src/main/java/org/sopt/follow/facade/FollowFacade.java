package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.domain.Follow;
import org.sopt.follow.exception.FollowAlreadyExistsException;
import org.sopt.follow.exception.FollowCoreErrorCode;
import org.sopt.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FollowFacade {

    private final FollowRetriever followRetriever;
    private final FollowSaver followSaver;

    private static final String UK_FOLLOWER_FOLLOWEE = "uk_follower_followee";

    /** 이미 팔로우 중이면 예외, 아니면 통과 */
    @Transactional(readOnly = true)
    public void assertNotFollowing(User follower, User followee) {
        followRetriever.assertNotFollowing(follower, followee);
    }

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

    private boolean isUniqueViolation(DataIntegrityViolationException e, String constraintName) {
        Throwable cause = e.getMostSpecificCause();
        String msg = (cause != null ? cause.getMessage() : "");
        return msg != null && msg.contains(constraintName);
    }

}