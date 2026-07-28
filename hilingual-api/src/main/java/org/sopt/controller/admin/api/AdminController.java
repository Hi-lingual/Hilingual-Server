package org.sopt.controller.admin.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.admin.dto.AttendanceReq;
import org.sopt.controller.admin.dto.AttendanceRes;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.service.AdminAttendanceService;
import org.sopt.controller.admin.service.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminNoticeService adminNoticeService;
    private final AdminAttendanceService adminAttendanceService;

    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateRes> create(
            @Valid @RequestBody NoticeCreateReq req
    ) {
        return ResponseEntity.ok(adminNoticeService.create(req));
    }

    @PostMapping("/notices/{noticeId}/delivery")
    public ResponseEntity<Void> deliver(
            @PathVariable Long noticeId
    ) {
        adminNoticeService.deliver(noticeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceRes> getAttendance(
            @Valid @RequestBody AttendanceReq req
    ) {
        return ResponseEntity.ok(adminAttendanceService.getAttendance(req));
    }
}

