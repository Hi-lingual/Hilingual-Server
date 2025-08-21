package org.sopt.aws.s3.dto;

public record PreSignedUrlRes(
    String fileKey,
    String uploadUrl
) {

    public static PreSignedUrlRes of(
            final String fileKey,
            final String uploadUrl
    ){
        return new PreSignedUrlRes(fileKey, uploadUrl);
    }
}