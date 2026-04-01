---
description: 하이링구얼 기준 코드 리뷰
---

아래 기준으로 현재 변경사항을 리뷰해줘:
- Controller → Service → Facade → Repository 계층 준수
- @Transactional 범위 적절한가
- N+1 쿼리 발생 가능성
- ErrorCode 기반 예외 처리
- @UserId 인증 방식 준수
- Redis 캐시 정합성
- API 스펙 변경 여부
- JWT/Spring Security 영향
- 401/403/500 응답 매핑 적절성
- DTO 변경 시 클라이언트 영향
