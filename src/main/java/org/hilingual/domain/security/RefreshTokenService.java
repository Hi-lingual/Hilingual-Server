package org.hilingual.domain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(Long userId, String refreshToken, long ttlSeconds) {
        RefreshToken redisToken = new RefreshToken(
                String.valueOf(userId),
                refreshToken,
                ttlSeconds
        );
        refreshTokenRepository.save(redisToken);
    }

    public Optional<String> getRefreshToken(Long userId) {
        return refreshTokenRepository.findById(userId) // findById에 String userId 사용
                .map(RefreshToken::getRefreshToken);
    }

    public void delete(Long userId) {
        refreshTokenRepository.deleteById(userId); // deleteById에 String userId 사용
    }
}