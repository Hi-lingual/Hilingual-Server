package org.sopt.controller.external;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UploadPurposeReq(
        @NotNull
        String purpose,
        @NotNull
        @Pattern(regexp = "image/jpeg|image/png|image/webp")
        String contentType
) {
}