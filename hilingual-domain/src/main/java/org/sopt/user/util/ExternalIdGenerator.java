package org.sopt.user.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class ExternalIdGenerator {

    private static final String DELIMITER = ":";
    private static final String ALGORITHM = "SHA-256";

    private ExternalIdGenerator() {
    }

    public static String generate(final String provider, final String providerId) {
        if (provider == null || providerId == null) {
            throw new IllegalArgumentException("provider와 providerId는 null일 수 없습니다.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest((provider + DELIMITER + providerId).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash); // 소문자 hex → postgres encode(...,'hex')와 일치
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}