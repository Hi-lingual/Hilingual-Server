package org.sopt.controller.external;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.dto.PreSignedUrlRes;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned-urls")
    public ResponseEntity<PreSignedUrlRes> getPreSignedUrl(
            @UserId Long userId,
            @RequestBody @Valid UploadPurposeReq req
    ) {
        return ResponseEntity.ok(s3Service.getPreSignedUrls(userId, req.purpose(), req.contentType()));
    }
}