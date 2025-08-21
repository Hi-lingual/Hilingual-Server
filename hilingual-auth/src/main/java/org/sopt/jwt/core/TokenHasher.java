package org.sopt.jwt.core;

import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.TokenHashingException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class TokenHasher {

    private static final String HASH_ALGORITHM = "SHA-256";

    public String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new TokenHashingException(AuthErrorCode.TOKEN_HASHING_FAILED);
        }
    }
}