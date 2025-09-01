package org.sopt.controller.userprofile.dto;

import lombok.NonNull;
import org.sopt.aws.s3.dto.Purpose;

public record UserProfileReq(
        Image image,
        @NonNull String nickname,
        @NonNull Boolean adAlarmAgree
) {
    public record Image(
            String fileKey,
            Purpose purpose
    ) {}
}
