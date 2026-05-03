package org.sopt.userprofile.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.block.domain.QBlock;
import org.sopt.follow.domain.QFollow;
import org.sopt.userprofile.domain.QUserProfile;
import org.sopt.userprofile.dto.UserSearchDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserProfileQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 닉네임 키워드에 해당하는 유저 목록을 조회한다.
     * - 현재 유저 자신과 서로 차단 관계인 유저는 제외한다.
     * - 검색어로 시작하는 닉네임을 우선 정렬하고, 이후 닉네임 오름차순으로 정렬한다.
     * - 팔로우 여부(isFollowing, isFollowed)를 함께 조회한다.
     */
    public List<UserSearchDto> searchUserListByKeyword(Long currentUserId, String keyword, String startWithKeyword) {
        QUserProfile up = QUserProfile.userProfile;
        QFollow follow1 = new QFollow("follow1");
        QFollow follow2 = new QFollow("follow2");
        QBlock block1 = new QBlock("block1");
        QBlock block2 = new QBlock("block2");

        return queryFactory
                .select(Projections.constructor(UserSearchDto.class,
                        up.user.id,
                        up.profileImg,
                        up.nickname,
                        JPAExpressions.selectOne()
                                .from(follow1)
                                .where(follow1.follower.id.eq(currentUserId)
                                        .and(follow1.followee.id.eq(up.user.id)))
                                .exists(),
                        JPAExpressions.selectOne()
                                .from(follow2)
                                .where(follow2.follower.id.eq(up.user.id)
                                        .and(follow2.followee.id.eq(currentUserId)))
                                .exists()
                ))
                .from(up)
                .where(
                        up.user.id.ne(currentUserId),
                        up.nickname.like(keyword),
                        up.user.id.notIn(
                                JPAExpressions.select(block1.blocked.id)
                                        .from(block1)
                                        .where(block1.blocker.id.eq(currentUserId))
                        ),
                        up.user.id.notIn(
                                JPAExpressions.select(block2.blocker.id)
                                        .from(block2)
                                        .where(block2.blocked.id.eq(currentUserId))
                        )
                )
                .orderBy(
                        new CaseBuilder()
                                .when(up.nickname.like(startWithKeyword)).then(0)
                                .otherwise(1)
                                .asc(),
                        up.nickname.asc()
                )
                .fetch();
    }
}
