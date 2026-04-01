---
name: migration
description: Flyway 마이그레이션 파일 생성
---

# Flyway Migration Skill

## 절차
1. `hilingual-api/src/main/resources/db/migration/` 폴더 탐색
2. 현재 가장 높은 버전 번호 확인
3. `V{n+1}__{description}.sql` 형식으로 파일 생성
4. `ddl-auto: validate` 환경 기준으로 SQL 작성
5. 롤백 불가능한 작업(DROP, TRUNCATE 등)은 반드시 경고

## 규칙
- 파일명은 snake_case 사용
- 한 파일에 하나의 논리적 변경만
- 컬럼 추가 시 DEFAULT 값 고려
- 인덱스 추가 시 성능 영향 명시
