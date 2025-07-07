package org.hilingual.test;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.external.s3.S3Service;
import org.hilingual.external.s3.exception.S3SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/test/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponseDto<String>> uploadImage(
            @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = s3Service.uploadImage("diary/", image);
        return ResponseEntity.ok(BaseResponseDto.success(S3SuccessCode.S3_UPLOAD_SUCCESS, imageUrl));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDto<Void>> deleteImage(
            @RequestParam("key") String key
    ) {
        s3Service.deleteImage(key);
        return ResponseEntity.ok(BaseResponseDto.success(S3SuccessCode.S3_DELETE_SUCCESS));
    }

}