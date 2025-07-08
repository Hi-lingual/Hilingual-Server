package org.hilingual.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void save(Long userId, String refreshToken, long ttlSeconds) {
        Token redisToken = new Token(
                String.valueOf(userId),
                refreshToken,
                ttlSeconds
        );
        tokenRepository.save(redisToken);
    }

    public Optional<String> getRefreshToken(Long userId) {
        return tokenRepository.findById(userId) // findById에 String userId 사용
                .map(Token::getRefreshToken);
    }

    public void delete(Long userId) {
        tokenRepository.deleteById(userId); // deleteById에 String userId 사용
    }
}