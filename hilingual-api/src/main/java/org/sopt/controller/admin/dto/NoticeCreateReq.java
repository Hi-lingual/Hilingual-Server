package org.sopt.controller.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticeCreateReq(
        @NotBlank String category,               // "SYSTEM", "MARKETING"
        @NotBlank @Size(max = 100) String title,
        @NotBlank String content
) {
}
