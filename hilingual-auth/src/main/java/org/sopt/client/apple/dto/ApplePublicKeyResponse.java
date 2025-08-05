package org.sopt.client.apple.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApplePublicKeyResponse {
    private List<ApplePublicKeyDto> keys;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ApplePublicKeyDto {
        private String kty; // 키 타입 (ex. RSA)
        private String kid; // 키 ID
        private String use; // 퍼블릭 키 (ex. sig)
        private String alg; // 알고리즘 (ex. RS256)
        private String n;
        private String e;
    }
}