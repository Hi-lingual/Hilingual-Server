package org.sopt.controller.authcode.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.controller.authcode.exception.AuthCodeApiErrorCode;
import org.sopt.controller.authcode.exception.InvalidAuthCodeException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final StringRedisTemplate redisTemplate;

    public Void verifyAuthCode(final String code) {
        String key = "authCode:" + code;

        // Redis에 해당 키가 존재하는 경우 return
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 키가 존재하면, 인증 성공으로 간주 - 해당 키를 Redis에서 삭제
            redisTemplate.delete(key);
            log.info("인증 코드 {}가 사용되어 삭제되었습니다.", code);
            return null;
        }

        throw new InvalidAuthCodeException(AuthCodeApiErrorCode.INVALID_AUTH_CODE);
    }
}
