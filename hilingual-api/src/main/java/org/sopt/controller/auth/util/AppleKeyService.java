package org.sopt.controller.auth.util;

import io.jsonwebtoken.Jwts;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;

@Component
public class AppleKeyService {
    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    public String appleClientId;

    @Value("${apple.oauth.key-id}")
    private String appleKeyId;

    @Value("${apple.oauth.team-id}")
    private String appleTeamId;

    @Value("${apple.oauth.private-key-value}")
    private String applePrivateKey;

    private static final long THIRTY_DAYS_MS = 1000L * 60 * 60 * 24 * 30;

    public String makeClientSecretToken() {
        return Jwts.builder()
                .subject(appleClientId) // sub (Service ID / Client ID)
                .issuer(appleTeamId) // iss (Team ID)
                .issuedAt(new Date()) // iat
                .expiration(new Date(System.currentTimeMillis() + THIRTY_DAYS_MS)) // exp (최대 6개월)
                .audience() // <--- 인자 없이 호출
                .add("https://appleid.apple.com") // <--- AudienceBuilder에 add()
                .and() // <--- 다시 JwtBuilder로 돌아감
                .header() // 헤더 빌더 시작
                .keyId(appleKeyId) // kid (Key ID) 설정
                .and() // 다시 JWT 빌더로 돌아감
                .signWith(getPrivateKey(), Jwts.SIG.ES256) // 개인 키로 서명 (JJWT 0.12.0+ 필요)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] decodedKeyBytes = Base64.getDecoder().decode(applePrivateKey);
            String pemKeyContent = new String(decodedKeyBytes, StandardCharsets.UTF_8);

            PEMParser pemParser = new PEMParser(new StringReader(pemKeyContent));
            Object object = pemParser.readObject();

            if (object instanceof PrivateKeyInfo) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                throw new RuntimeException("애플 로그인 실패: 개인 키 파싱 실패 - 예상치 못한 형식");
            }
        } catch (Exception e) {
            throw new RuntimeException("애플 로그인 실패: 개인 키 로드 중 오류 발생", e);
        }
    }
}
