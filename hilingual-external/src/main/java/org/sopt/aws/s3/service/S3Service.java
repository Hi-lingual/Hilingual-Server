package org.sopt.aws.s3.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.config.AWSProperties;
import org.sopt.aws.s3.dto.PreSignedUrlRes;
import org.sopt.aws.s3.dto.Purpose;
import org.sopt.aws.s3.exception.S3BaseException;
import org.sopt.aws.s3.exception.S3ErrorCode;
import org.sopt.exception.code.GlobalErrorCode;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.utils.http.SdkHttpUtils;

import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.trimToNull;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final DateTimeFormatter DATE_DIR = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Map<String, String> CONTENT_TYPE_TO_EXT = Map.of(
            "image/jpeg", "jpg",
            "image/png",  "png",
            "image/webp", "webp"
    );
    private static final Duration DEFAULT_PRESIGN_TTL = Duration.ofMinutes(10);

    private final S3Presigner presigner;
    private final S3Client s3Client;
    private final AWSProperties awsProperties;
    private final Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));

    /*
    업로드용 presigned URL 발급 메서드
     */
    public PreSignedUrlRes getPreSignedUrls(Long userId, String purpose, String contentType) {

        final String bucket = awsProperties.getBucketName();
        final Purpose pur = Purpose.from(purpose);
        final String ext = extFromContentType(contentType);

        final String rawTmpKey = buildTmpKey(userId, pur, ext);
        final String fileKey = prefix(rawTmpKey);

        try {
            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(contentType)
                    .build();

            PresignedPutObjectRequest p = presigner.presignPutObject(b -> b
                    .putObjectRequest(put)
                    .signatureDuration(DEFAULT_PRESIGN_TTL));
            URL url = p.url();
            return PreSignedUrlRes.of(fileKey, url.toString());
        } catch (S3Exception e) {
            throw mapS3(e, S3ErrorCode.PRESIGN_CREATE_FAILED);
        } catch (SdkException e) {
            throw new S3BaseException(S3ErrorCode.PRESIGN_CREATE_FAILED);
        }
    }

    private S3BaseException mapS3(S3Exception e, S3ErrorCode fallback) {
        return switch (e.statusCode()) {
            case 401 -> new S3BaseException(S3ErrorCode.AWS_CREDENTIALS_MISSING);
            case 403 -> new S3BaseException(S3ErrorCode.S3_ACCESS_DENIED);
            case 503 -> new S3BaseException(S3ErrorCode.S3_SERVICE_UNAVAILABLE);
            default -> new S3BaseException(fallback);
        };
    }

    private static String extFromContentType(String ct) {
        String ext = CONTENT_TYPE_TO_EXT.get(ct);
        if (ext == null) throw new S3BaseException(S3ErrorCode.UNSUPPORTED_CONTENT_TYPE);
        return ext;
    }

    private String buildTmpKey(Long userId, Purpose purpose, String ext) {
        return switch (purpose) {
            case PROFILE_UPLOAD, PROFILE_UPDATE -> "users/%d/images/profile/tmp/%s.%s"
                    .formatted(userId, UUID.randomUUID(), ext);
            case DIARY_IMAGE -> {
                String dateDir = LocalDate.now(clock).format(DATE_DIR);
                yield "users/%d/images/diaries/tmp/%s/%s.%s"
                        .formatted(userId, dateDir, UUID.randomUUID(), ext);
            }
        };
    }

    /*
     * tmp 키를 최종 위치로 이동(finalKey 반환) 후 tmp 삭제
     */
    public String bindDiaryImage(Long userId, String tmpKey, LocalDate date) {
        String expectedPrefix = prefix("users/%d/images/diaries/tmp/".formatted(userId));
        validateTmpKeyOwnership(userId, tmpKey, expectedPrefix);

        String dateDir = date.toString();
        String filename = tmpKey.substring(tmpKey.lastIndexOf('/') + 1);
        String bucket = awsProperties.getBucketName();
        String finalKey = prefix("users/%d/images/diaries/%s/%s".formatted(userId, dateDir, filename));

        String copySourceRaw = bucket + "/" + tmpKey; // tmpKey는 이미 prefix 포함
        String copySource = SdkHttpUtils.urlEncode(copySourceRaw);

        try {
            s3Client.copyObject(CopyObjectRequest.builder()
                    .copySource(copySource)
                    .destinationBucket(bucket)
                    .destinationKey(finalKey)
                    .build());
        } catch (SdkException e) {
            throw new S3BaseException(S3ErrorCode.S3_COPY_FAILED);
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(tmpKey)
                    .build());
        } catch (SdkException e) {
            throw new S3BaseException(S3ErrorCode.S3_DELETE_FAILED);
        }
        return finalKey;
    }

    private static void validateTmpKeyOwnership(Long userId, String tmpKey, String expectedPrefix) {
        if (!Objects.requireNonNull(tmpKey, "tmpKey").startsWith(expectedPrefix)) {
            throw new S3BaseException(S3ErrorCode.INVALID_TMP_FILE_KEY);
        }
    }

    // 모든 S3 key 앞에 prefix(dev/prod) 안전하게 붙임
    private String prefix(String key) {
        String p = trimToNull(awsProperties.getPrefix()); // "dev" / "prod"
        String k = normalize(key);
        if (p == null) return k;

        String np = normalize(p);
        if (k.startsWith(np + "/")) return k;
        return np + "/" + k;
    }

    private static String normalize(String s) {
        String v = Objects.requireNonNull(s).trim();
        while (v.startsWith("/")) v = v.substring(1);
        while (v.endsWith("/")) v = v.substring(0, v.length() - 1);
        return v;
    }

    /*
     * DB에 저장된 S3 key를 퍼블릭 URL로 변환
     * TODO : https 연결 확인하기
     */
    public String toPublicUrl(String key) {
        if (key == null || key.isBlank()) return null;
        String cdn = trimToNull(awsProperties.getCdnDomain());
        if (cdn == null) throw new S3BaseException(S3ErrorCode.CDN_DOMAIN_NOT_CONFIGURED);

        String normKey = key.startsWith("/") ? key.substring(1) : key;

        String base = cdn.startsWith("http://")
                ? cdn
                : "http://" + (cdn.endsWith("/") ? cdn.substring(0, cdn.length() - 1) : cdn);

        return base + "/" + normKey;
    }
}