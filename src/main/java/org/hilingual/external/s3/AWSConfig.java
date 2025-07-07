package org.hilingual.external.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    private final AWSProperties awsProperties;

    @Bean
    public S3Client s3Client() {
        boolean hasAccessKey = awsProperties.getAccessKey() != null && !awsProperties.getAccessKey().isBlank();
        boolean hasSecretKey = awsProperties.getSecretKey() != null && !awsProperties.getSecretKey().isBlank();

        if (hasAccessKey && hasSecretKey) {
            return S3Client.builder()
                    .region(Region.of(awsProperties.getAwsRegion()))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                            awsProperties.getAccessKey(),
                                            awsProperties.getSecretKey()
                                    )
                            )
                    )
                    .build();
        } else {
            return S3Client.builder()
                    .region(Region.of(awsProperties.getAwsRegion()))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
    }
}