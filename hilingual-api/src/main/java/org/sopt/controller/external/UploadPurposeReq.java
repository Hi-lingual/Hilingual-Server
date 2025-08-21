package org.sopt.controller.external;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UploadPurposeReq(
        @NotBlank(message = "purpose는 필수입니다.")
        String purpose,
        @NotBlank(message = "contentType은 필수입니다.")
        @Pattern(
                regexp = "^(image/jpeg|image/png|image/webp)$",
                message = "지원하지 않는 contentType 입니다. (image/jpeg|image/png|image/webp)"
        )
        String contentType
) {
}