package org.sopt.jwt.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, String> {

    // 사용자 ID로 모든 토큰 조회 (멀티 디바이스 지원)
    List<Token> findByUserId(Long userId);

    // 사용자 ID로 모든 토큰 삭제 (회원탈퇴 시 사용)

    // 멀티디바이스를 고려해 userId:sessionId를 id로 갖는 토큰 삭제 (로그아웃 시 사용)
}