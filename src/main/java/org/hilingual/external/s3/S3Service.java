package org.hilingual.external.s3;

import lombok.RequiredArgsConstructor;
import org.hilingual.external.s3.exception.InvalidImageException;
import org.hilingual.external.s3.exception.S3ApiException;
import org.hilingual.external.s3.exception.S3ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Service {

    private final AWSProperties awsProperties;
    private final S3Client s3Client;

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            "image/jpeg", "image/png", "image/jpg", "image/webp", "image/heic", "image/heif"
    );
    private static final Long MAX_FILE_SIZE = 7 * 1024 * 1024L;

    public String uploadImage(String directoryPath, MultipartFile image) {

        System.out.println("메서드 진입");

        validateExtension(image);
        validateFileSize(image);

        final String key = directoryPath + generateImageFileName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        try {
            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            s3Client.putObject(request, requestBody);
            return key;
        } catch (IOException e) {
            throw new S3ApiException(S3ErrorCode.FILE_PROCESSING_FAILED);
        } catch (S3Exception e) {
            throw new S3ApiException(S3ErrorCode.S3_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(key)
                .build();

        try {
            s3Client.deleteObject(request);
        } catch (S3Exception e) {
            throw new S3ApiException(S3ErrorCode.S3_DELETE_FAILED);
        }
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new InvalidImageException(S3ErrorCode.INVALID_IMAGE_EXTENSION);
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException(S3ErrorCode.INVALID_IMAGE_SIZE);
        }
    }
}