package org.hilingual.external.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Service {

    private final AWSProperties awsProperties;
    private final S3Client s3Client;

    private static final Map<String, String> EXTENSION_MAP = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/heic", ".heic",
            "image/heif", ".heif"
    );

    private static final List<String> ALLOWED_CONTENT_TYPES = List.copyOf(EXTENSION_MAP.keySet());
    private static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    public String uploadImage(String directoryPath, MultipartFile image) {

        validateExtension(image);
        validateFileSize(image);

        final String key = buildKey(directoryPath, image);

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

    private String buildKey(String directoryPath, MultipartFile image) {
        String extension = EXTENSION_MAP.getOrDefault(image.getContentType(), ".jpg");
        String prefix = directoryPath.endsWith("/") ? directoryPath : directoryPath + "/";
        return prefix + UUID.randomUUID() + extension;
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidImageException(S3ErrorCode.INVALID_IMAGE_EXTENSION);
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException(S3ErrorCode.INVALID_IMAGE_SIZE);
        }
    }
}