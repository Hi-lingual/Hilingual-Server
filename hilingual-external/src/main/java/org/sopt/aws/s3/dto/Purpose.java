package org.sopt.aws.s3.dto;

import lombok.Getter;
import org.sopt.aws.s3.exception.S3BaseException;
import org.sopt.aws.s3.exception.S3ErrorCode;

@Getter
public enum Purpose {
    PROFILE_UPLOAD,
    PROFILE_UPDATE,
    DIARY_IMAGE;

    public static Purpose from(String raw) {
        try {
            return Purpose.valueOf(raw.trim().toUpperCase());
        } catch (Exception e) {
            throw new S3BaseException(S3ErrorCode.INVALID_IMAGE_PURPOSE);
        }
    }
}