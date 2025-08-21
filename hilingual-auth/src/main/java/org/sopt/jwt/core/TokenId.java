package org.sopt.jwt.core;

import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.InvalidTokenException;

public record TokenId(long userId, String sessionId) {

    private static final String DELIMITER = ":";

    @Override
    public String toString() {
        return userId + DELIMITER + sessionId;
    }

    public static TokenId from(String tokenId) {
        int idx = tokenId.indexOf(DELIMITER);
        if (idx < 0) throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN_ID);

        long uid = Long.parseLong(tokenId.substring(0, idx));
        String sid = tokenId.substring(idx + 1);

        return new TokenId(uid, sid);
    }
}