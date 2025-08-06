package org.sopt.controller.test.api;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.exception.S3SuccessCode;
import org.sopt.aws.s3.utils.S3Service;
import org.sopt.dto.BaseResponseDto;
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