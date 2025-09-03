package org.sopt.diary.facade;


import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryUpdater {

    public void publish(Diary diary) {
        diary.publish();
    }

    public void unpublish(Diary diary) {
        diary.unpublish();
    }

    public void increaseLike(Diary diary) {
        diary.increaseLikeCount();
    }

    public void decreaseLike(Diary diary) {
        diary.decreaseLikeCount();
    }

}