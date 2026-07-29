package org.sopt.controller.admin.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.admin.dto.AttendanceReq;
import org.sopt.controller.admin.dto.AttendanceRes;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAttendanceService {

    private final UserFacade userFacade;
    private final DiaryFacade diaryFacade;

    @Transactional(readOnly = true)
    public AttendanceRes getAttendance(final AttendanceReq req) {
        // 1) externalId -> User 매핑
        List<User> users = userFacade.getByExternalIdIn(req.externalIds());
        Map<String, User> userByExternalId = users.stream()
                .collect(Collectors.toMap(User::getExternalId, u -> u));

        // 2) 매칭된 userId로 공유 일기 벌크 조회 (IN + BETWEEN + isPublic)
        List<Long> userIds = users.stream().map(User::getId).toList();
        Map<Long, List<Diary>> diariesByUserId = userIds.isEmpty()
                ? Map.of()
                : diaryFacade.getSharedDiaries(userIds, req.startDate(), req.endDate()).stream()
                  .collect(Collectors.groupingBy(d -> d.getUser().getId()));

        // 3) 요청 externalId 순서대로 participant 조립
        List<AttendanceRes.Participant> participants = req.externalIds().stream()
                .map(externalId -> toParticipant(
                        externalId,
                        userByExternalId.get(externalId),
                        diariesByUserId,
                        req.startDate(),
                        req.endDate()))
                .toList();

        return AttendanceRes.of(participants);
    }

    private AttendanceRes.Participant toParticipant(
            final String externalId,
            final User user,
            final Map<Long, List<Diary>> diariesByUserId,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        // 미매칭 externalId → userId null, days 빈 배열
        if (user == null) {
            return AttendanceRes.Participant.of(externalId, null, List.of());
        }

        // writtenDate -> Diary (unique(user_id, written_date)라 날짜당 최대 1개)
        Map<LocalDate, Diary> sharedByDate = diariesByUserId.getOrDefault(user.getId(), List.of()).stream()
                .collect(Collectors.toMap(Diary::getWrittenDate, d -> d, (a, b) -> a));

        // 기간 내 모든 날짜를 채워 공유 여부 판정
        List<AttendanceRes.DayAttendance> days = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> {
                    Diary diary = sharedByDate.get(date);
                    return (diary == null)
                            ? AttendanceRes.DayAttendance.absent(date)
                            : AttendanceRes.DayAttendance.shared(date, diary.getCreatedAt(), diary.getIsRecovered());
                })
                .toList();

        return AttendanceRes.Participant.of(externalId, user.getId(), days);
    }
}