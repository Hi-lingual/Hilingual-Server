package org.sopt.client.apple;


import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;
import lombok.extern.slf4j.Slf4j;
import org.sopt.client.apple.dto.ApplePublicKeyResponse;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
public class MyKeyLocator extends LocatorAdapter<Key> {

    private final List<ApplePublicKeyResponse.ApplePublicKeyDto> publicKeyList;

    public MyKeyLocator(List<ApplePublicKeyResponse.ApplePublicKeyDto> publicKeyList) {
        this.publicKeyList = publicKeyList;
    }

    @Override
    protected Key locate(JwsHeader header) {
        log.info("[MyKeyLocator] JWT Header: {}", header);

        ApplePublicKeyResponse.ApplePublicKeyDto publicKey = publicKeyList.stream()
                .filter(applePublicKey -> applePublicKey.getKid().equals(header.getKeyId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[MyKeyLocator] 일치하는 public key를 찾을 수 없습니다. 요청된 kid: {}, 현재 보유한 public key 목록: {}", header.getKeyId(), publicKeyList.toString());
                    return new RuntimeException(
                            "일치하는 public key가 없습니다. 요청된 kid: " + header.getKeyId() +
                                    ", 보유한 public key 목록: " + publicKeyList
                    );
                });

        BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.getN()));
        BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(publicKey.getE()));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new RSAPublicKeySpec(n, e);
            Key resultKey = keyFactory.generatePublic(keySpec);

            log.info("[MyKeyLocator] Public Key 추출 성공. kid: {}", header.getKeyId());
            return resultKey;
        } catch (Exception error) {
            log.error("[MyKeyLocator] public key 추출 실패: {}", error.getMessage(), error);
            throw new RuntimeException("[애플 로그인] public key 추출 실패: " + error.getMessage(), error);
        }
    }
}