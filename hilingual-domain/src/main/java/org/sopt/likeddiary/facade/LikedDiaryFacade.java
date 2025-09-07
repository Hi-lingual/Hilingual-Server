package org.sopt.likeddiary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.likeddiary.domain.LikedDiary;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedDiaryFacade {

    private final LikedDiaryRetriever likedDiaryRetriever;
    private final LikedDiarySaver likedDiarySaver;
    private final LikedDiaryRemover likedDiaryRemover;

    public List<Long> findLikedDiaryIdsByUserIdAndDiaryIdsIn(Long userId, List<Long> diaryIds) {
        return likedDiaryRetriever.findLikedDiaryIdsByUserIdAndDiaryIdsIn(userId, diaryIds);
    }

    public List<LikedDiary> getLikedDiariesWithAllDetails(Long userId) {
        return likedDiaryRetriever.getLikedDiariesWithAllDetails(userId);
    }

    public Boolean findUserAndDiaryExist(Long userId, Long diaryId) {
        return likedDiaryRetriever.findUserAndDiaryExist(userId, diaryId);
    }

    @Transactional
    public void like(User user, Diary diary) {
        if (!findUserAndDiaryExist(user.getId(), diary.getId())) {
            likedDiarySaver.save(LikedDiary.create(user, diary));
        }
    }

    @Transactional
    public void unlike(Long userId, Long diaryId) {
        likedDiaryRemover.deleteByUserIdAndDiaryId(userId, diaryId);
    }

    @Transactional
    public void unlikeAllOfDiary(Long diaryId) {
        likedDiaryRemover.deleteByDiaryId(diaryId);
    }

}
