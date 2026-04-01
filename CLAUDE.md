# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build all modules
./gradlew build

# Build without tests
./gradlew build -x test

# Run the application (hilingual-api module)
./gradlew :hilingual-api:bootRun

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :hilingual-api:test
./gradlew :hilingual-domain:test

# Run a single test class
./gradlew :hilingual-api:test --tests "org.sopt.controller.diary.DiaryServiceTest"

# Clean build
./gradlew clean build

# Check for unused dependencies
./gradlew buildHealth
```

## Module Architecture

This is a multi-module Spring Boot 3.4 / Java 17 project with a strict dependency hierarchy:

```
hilingual-api          ← Entry point: Controllers + Services (application layer)
  ↓ depends on
hilingual-domain       ← JPA entities, Facades, Repositories, QueryDSL
hilingual-auth         ← JWT token handling, Spring Security filters
hilingual-external     ← AWS S3, OpenAI (Feign client)
hilingual-common       ← Shared DTOs, exception base classes, response envelope
```

Only `hilingual-api` produces a bootJar (`app.jar`). All other modules produce plain JARs.

### Module Responsibilities

- **hilingual-api**: HTTP layer only. `controller/<feature>/api/` for controllers, `controller/<feature>/service/` for use-case services that orchestrate domain facades. Each feature has its own `exception/` sub-package with `ApiErrorCode` and `ApiException`.
- **hilingual-domain**: Domain entities live under `org/sopt/<aggregate>/domain/`. Each aggregate has a `facade/` that groups `Retriever`, `Saver`, `Remover`, `Updater` components. Repositories extend Spring Data JPA; QueryDSL queries go in separate `*QueryRepository` classes. QueryDSL Q-types are generated into `build/generated/querydsl`.
- **hilingual-auth**: JWT `JwtTokenProvider`, `JwtAuthenticationFilter`, `UserAuthentication`, and Redis-backed `TokenRepository`. The `@UserId` annotation extracts the authenticated user ID in controllers.
- **hilingual-external**: `S3Service` for pre-signed URL generation and file operations. `OpenAIService` via Feign client for GPT diary feedback.
- **hilingual-common**: `BaseResponseDto<T>` response envelope (code + data + message). `HilingualBaseException` is the root for all domain exceptions. `ErrorCode` / `SuccessCode` interfaces with `GlobalErrorCode` / `GlobalSuccessCode` implementations.

## Key Patterns

### Response Format

All API responses use `BaseResponseDto<T>`:
```java
BaseResponseDto.success(GlobalSuccessCode.SUCCESS, data)
BaseResponseDto.fail(SomeErrorCode.NOT_FOUND)
```

### Exception Hierarchy

- Domain exceptions: extend module-level `*CoreException` → `HilingualBaseException`
- API exceptions: extend module-level `*ApiException` → `HilingualBaseException`
- Error codes implement `ErrorCode` interface with `getCode()` and `getMessage()`
- `GlobalExceptionHandler` in `hilingual-api` handles all exceptions centrally

### Facade Pattern

Facades (`@Component`) in `hilingual-domain` aggregate Retriever/Saver/Remover/Updater components. Services in `hilingual-api` call facades — they do not directly access repositories. Facades own `@Transactional` boundaries for write operations; services that span multiple facades coordinate transactions at the service level.

### Authentication

`@UserId Long userId` parameter injection is available in all controllers via the custom `UserId` resolver. The `SecurityUtils.getCurrentUserId()` utility can also be used within services.

## Database & Migrations

- PostgreSQL with Flyway migrations in `hilingual-api/src/main/resources/db/migration/`
- Migration files follow `V{n}__{description}.sql` naming
- `ddl-auto: validate` in production (non-local profiles) — schema changes require a migration file
- `ddl-auto: create` in local profile — Flyway is disabled locally
- Blue/green deployment via Spring profiles (`blue` port 8081, `green` port 8082)

## Environment Variables

All secrets come from a `.env` file (loaded via `dotenv-java`). Required variables:
`DB_URL`, `DB_ID`, `DB_PW`, `SECRET_KEY`, `ACCESS_EXPIRATION`, `REFRESH_EXPIRATION`, `REDIS_HOST`, `REDIS_PORT`, `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`, `AWS_REGION`, `AWS_BUCKET_NAME`, `AWS_CDN_DOMAIN`, `AWS_S3_FILE_KEY`, `OPENAI_API_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `NEW_USER_WEBHOOK`

For local development: copy `.env.example` to `.env` and fill in values. Use `application-local.yml` profile.

## Infrastructure

Docker Compose runs blue/green Spring instances + Redis + Promtail (log shipping to Loki/Grafana). Monitoring via Prometheus (`/actuator/prometheus`) and Micrometer with histogram percentiles.
