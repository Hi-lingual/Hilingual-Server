package org.sopt.controller.auth.util;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.auth.exception.AppleServerErrorException;
import org.sopt.controller.auth.exception.AuthApiErrorCode;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
public class MyKeyLocator extends LocatorAdapter<Key> {

    private final List<ApplePublicKeyList.ApplePublicKey> publicKeyList;

    @Override
    protected Key locate(JwsHeader header) {

        ApplePublicKeyList.ApplePublicKey publicKey = publicKeyList.stream()
                .filter(applePublicKey -> applePublicKey.kid().equals(header.getKeyId()))
                .findFirst()
                .orElseThrow(() -> {
                    // 일치하는 public key를 찾을 수 없음
                    return new AppleServerErrorException(AuthApiErrorCode.AUTH_APPLE_SERVER_ERROR);
                });

        BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.n()));
        BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.e()));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new RSAPublicKeySpec(n, e);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception error) {
            // Public key 추출 실패
            throw new AppleServerErrorException(AuthApiErrorCode.AUTH_APPLE_SERVER_ERROR);
        }
    }
}