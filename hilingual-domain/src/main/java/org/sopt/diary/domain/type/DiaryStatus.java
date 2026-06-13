package org.sopt.diary.domain.type;

public enum DiaryStatus {
    WRITTEN,    // 일반 작성 완료
    RECOVERED,  // 부활권으로 작성 완료
    UNLOCKED    // 광고 시청 후 미작성 (해금됨)
}