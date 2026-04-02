# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

---

## Build & Run Commands

- Build all modules: `./gradlew build`
- Build without tests: `./gradlew build -x test`
- Run app: `./gradlew :hilingual-api:bootRun`
- Run all tests: `./gradlew test`
- Module test:
  - `./gradlew :hilingual-api:test`
  - `./gradlew :hilingual-domain:test`
- Single test:
  - `./gradlew :hilingual-api:test --tests "org.sopt.controller.diary.DiaryServiceTest"`
- Clean build: `./gradlew clean build`
- Dependency health: `./gradlew buildHealth`

---

## Module Architecture

hilingual-api → Controllers + Services  
↓  
hilingual-domain → Entities, Facade, Repository, QueryDSL  
hilingual-auth → JWT, Security  
hilingual-external → S3, OpenAI  
hilingual-common → DTO, Exception, Response

- Only `hilingual-api` produces bootJar

---

## Module Responsibilities

- hilingual-api: Controller + Use-case Service
- hilingual-domain: Entity + Facade + QueryDSL
- hilingual-auth: JWT 인증
- hilingual-external: 외부 연동
- hilingual-common: 공통 DTO/Exception

---

## Key Patterns

### Response

- BaseResponseDto 사용
- success / fail 패턴 유지

### Exception

- CoreException / ApiException 분리
- ErrorCode 기반
- GlobalExceptionHandler에서 처리

### Facade

- Service → Facade → Repository 구조
- Service는 Repository 직접 접근 금지

### Authentication

- Controller: @UserId
- Service: SecurityUtils.getCurrentUserId()

---

## Database

- PostgreSQL + Flyway
- db/migration
- V{n}__{desc}.sql
- local: create
- prod: validate
- blue/green: 8081 / 8082

---

## Environment

.env 기반

- DB_URL, DB_ID, DB_PW
- SECRET_KEY, ACCESS_EXPIRATION
- REDIS_HOST, REDIS_PORT
- AWS_*, OPENAI_API_KEY

---

## Infrastructure

- Docker Compose
- Redis
- Prometheus / Grafana
- Micrometer

---

## Codex 작업 규칙

- 한국어로 설명 + 결론 먼저
- 코드 작성 전 구조 분석 필수
- 기존 구조/패턴 유지
- 새 구조 도입 시 이유 설명
- 여러 파일 수정 시 이유 명시
- 기존 코드 패턴과 다른 구현을 할 경우 반드시 이유를 설명한다
- 추측으로 구현하지 말고, 불확실하면 먼저 질문한다

---

## 아키텍처 원칙

- Controller → Service → Facade → Repository
- Service는 Facade만 호출
- 예외는 GlobalExceptionHandler
- 401 / 403 / 500 구분

---

## API 설계 기준

- BaseResponseDto 사용
- Controller에 로직 금지
- DTO 분리
- API 변경 시 클라이언트 영향 설명

---

## 성능 기준

- N+1 체크
- Fetch 전략 고려
- QueryDSL → QueryRepository
- Redis 캐시 invalidation 고려

---

## 인증 규칙

- @UserId 사용
- Security Filter Chain 고려
- 인증 실패 → 401

---

## 변경 전 체크리스트

- [ ] @Transactional 적절한가?
- [ ] N+1 문제 없는가?
- [ ] Redis 정합성 유지되는가?
- [ ] 클라이언트 영향 있는가?

---

## 금지사항

- 라이브러리 임의 추가 금지
- 엔티티 구조 대규모 변경 금지
- API 스펙 임의 변경 금지
- .env 직접 수정 금지  
