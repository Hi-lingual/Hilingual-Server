package org.sopt.controller.userprofile.dto;

import jakarta.validation.constraints.NotNull;
import org.sopt.aws.s3.dto.Purpose;

public record UserProfileImgReq(
        Image image
) {
    public record Image(
            @NotNull(message = "fileKey는 필수입니다.")
            String fileKey,
            @NotNull
            Purpose purpose
    ) {}
}
