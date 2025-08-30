package org.sopt.controller.auth.dto;

import java.util.List;

public record ApplePublicKeyRes(
        List<ApplePublicKey> keys
) {

    public record ApplePublicKey(
            String kty, // 키 타입 (ex. RSA)
            String kid, // 키 ID
            String use, // 퍼블릭 키 (ex. sig)
            String alg, // 알고리즘 (ex. RS256)
            String n,
            String e
    ) {
    }
}