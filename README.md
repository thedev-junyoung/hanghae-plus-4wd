# 🧩 STEP07 - Integration 과제

 **Infrastructure Layer 구현 및 통합 테스트** 

---

## ⚙️ 실행 방법

### 1. DB 및 초기 데이터 실행

```bash
# 전체 MySQL 초기화 + 스키마 및 데이터 자동 적용
./init/reset-db.sh
```

MySQL 컨테이너 완전 초기화 (docker volume 삭제) 

init/01-schema.sql, init/02-data.sql 순서대로 실행

./data/mysql 디렉토리도 초기화됨 → 완전 새 상태에서 테스트 가능


### 2. 테스트 실행 방법

```bash
./gradlew test
```

- **Testcontainers 기반 통합 테스트** 실행
- `application.yml`에 따로 DB 정보 설정할 필요 없이 자동으로 테스트 컨테이너가 뜹니다.
- 테스트마다 `@Transactional` 및 테스트용 DB를 사용하므로 **안정적이고 격리된 테스트** 가능

---

### 3. 테스트 구조 설계 의도

> 각 계층별로 테스트 책임을 명확히 분리했습니다.
>

| 계층 | 테스트 클래스 | 목적 |
| --- | --- | --- |
| Service | `*ServiceTest` | 유닛 테스트 (비즈니스 로직 단위 검증) |
| Service (Integration) | `*ServiceIntegrationTest` | DB 반영 여부 최소 검증 (필요한 경우만) |
| Facade | `*FacadeIntegrationTest` | 흐름 테스트 (도메인 간 협력 포함한 end-to-end 시나리오 검증) |