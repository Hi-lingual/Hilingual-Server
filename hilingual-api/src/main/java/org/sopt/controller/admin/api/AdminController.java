package org.sopt.controller.admin.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.service.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminNoticeService adminNoticeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateRes> create(
            @Valid @RequestBody NoticeCreateReq req
    ) {
        return ResponseEntity.ok(adminNoticeService.create(req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notices/{noticeId}/delivery")
    public ResponseEntity<Void> deliver(
            @PathVariable Long noticeId
    ) {
        adminNoticeService.deliver(noticeId);
        return ResponseEntity.ok().build();
    }
}

