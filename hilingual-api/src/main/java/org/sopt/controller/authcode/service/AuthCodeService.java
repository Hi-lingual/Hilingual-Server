package org.sopt.controller.authcode.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.authcode.exception.AuthCodeApiErrorCode;
import org.sopt.controller.authcode.exception.InvalidAuthCodeException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final StringRedisTemplate redisTemplate;
    private final Random random = new Random();

    /*
     * 테스트를 위해 임시로 작성한 코드
     * 6자리 인증 코드를 1000개 생성하여 Redis에 저장
     * 각 코드는 10분 후 만료
     */
    @PostConstruct
    public void generateAndSaveAuthCodes() {
        for (int i = 0; i < 1000; i++) {
            // 100000부터 999999 사이의 6자리 랜덤 숫자 생성
            int code = 100000 + random.nextInt(900000);
            String authCode = String.valueOf(code);

            // Redis 키 생성
            String key = "authCode:" + authCode;

            // Redis에 키를 저장하고 10분 TTL(Time-To-Live) 설정
            redisTemplate.opsForValue().set(key, "valid", 10, TimeUnit.MINUTES);
        }

        printAllAuthCodes();
    }

    /*
     * 테스트를 위해 임시로 작성한 코드
     * Redis에 저장된 모든 인증 코드 삭제
     */
    public void deleteAllAuthCodes() {
        Set<String> keys = redisTemplate.keys("authCode:*");
        if (keys != null && !keys.isEmpty()) {
            Long deletedCount = redisTemplate.delete(keys);
            System.out.println("서버 종료 시 Redis에서 총 " + deletedCount + "개의 인증 코드가 삭제되었습니다.");
        }
    }

    /*
     * 테스트를 위해 임시로 작성한 코드
     * 인증 코드 생성 확인
     */
    public void printAllAuthCodes() {
        Set<String> keys = redisTemplate.keys("authCode:*");

        if (keys != null && !keys.isEmpty()) {
            System.out.println("--- Redis에 저장된 인증 코드 목록 ---");
            for (String key : keys) {
                String authCode = key.substring("authCode:".length());
                String value = redisTemplate.opsForValue().get(key);
                System.out.println("인증 코드: " + authCode + ", 값: " + value);
            }
            System.out.println("--- 총 " + keys.size() + "개의 인증 코드가 있습니다. ---");
        } else {
            System.out.println("Redis에 저장된 인증 코드가 없습니다.");
        }
    }

    public Void verifyAuthCode(final int code) {
        String key = "authCode:" + code;

        // Redis에 해당 키가 존재하는 경우 return
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 키가 존재하면, 인증 성공으로 간주 - 해당 키를 Redis에서 삭제
            redisTemplate.delete(key);
            System.out.println("인증 코드 " + code + "가 사용되어 삭제되었습니다.");
            return null;
        }

        throw new InvalidAuthCodeException(AuthCodeApiErrorCode.INVALID_AUTH_CODE);
    }
}
