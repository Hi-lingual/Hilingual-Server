package org.hilingual.external.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws-property")
public class AWSProperties {
    private String accessKey;
    private String secretKey;
    private String awsRegion;
    private String bucketName;
}