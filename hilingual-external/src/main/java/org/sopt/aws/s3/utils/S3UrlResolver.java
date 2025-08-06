package org.sopt.aws.s3.utils;

public class S3UrlResolver {

    private static final String S3_BASE_URL = "https://hilingual-bucket.s3.ap-northeast-2.amazonaws.com/";

    private S3UrlResolver() {}

    public static String resolve(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return null;
        return imageUrl.startsWith("http") ? imageUrl : S3_BASE_URL + imageUrl;
    }

}
