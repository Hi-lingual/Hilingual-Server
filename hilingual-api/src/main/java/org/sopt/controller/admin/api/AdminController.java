package org.sopt.controller.admin.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.service.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminNoticeService adminNoticeService;

    @PostMapping("/notices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticeCreateRes> create(
            @Valid @RequestBody NoticeCreateReq req
    ) {
        return ResponseEntity.ok(adminNoticeService.create(req));
    }

}

