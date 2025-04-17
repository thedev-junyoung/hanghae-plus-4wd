# **STEP08 - DB & 동시성 테스트 과제**

## **프로젝트 개요**

이 과제는 **DB 성능 최적화** 및 **동시성 문제 해결**을 위한 프로젝트입니다. 주로 **인기 상품 조회**, **상품 조회** API에서 발생할 수 있는 성능 병목을 개선하고, 여러 서비스가 동시작업 시 발생할 수 있는 동시성 문제를 사전에 분석하고 해결합니다.

## **주요 테스트**

### 1. **동시성 테스트**
  - **주문 재고 차감 동시성**: 여러 사용자가 동시에 같은 상품을 주문할 때 발생할 수 있는 재고 차감 문제를 검증합니다.

    [**OrderConcurrencyTest**](src/test/java/kr/hhplus/be/server/application/order/OrderConcurrencyTest.java)

  - **잔액 충전 동시성**: 여러 사용자가 동시에 잔액을 충전할 때 발생할 수 있는 동시성 문제를 검증합니다.

    [**BalanceConcurrencyTest**](/src/test/java/kr/hhplus/be/server/application/balance/BalanceConcurrencyTest.java)

  - **쿠폰 발급 동시성**: 여러 사용자가 동시에 같은 쿠폰을 발급받을 때 발생할 수 있는 동시성 문제를 검증합니다.

    [**CouponConcurrencyTest**](src/test/java/kr/hhplus/be/server/application/coupon/CouponConcurrencyTest.java)
    
    
### [2. **성능 최적화 보고서 **](./report)
  - **인기 상품 조회** 성능 최적화

    [**popular-products-performance.md**](report/popular-products-performance.md)

  - **상품 목록 조회 - created_at 기준 정렬** 성능 최적화

    [**product-list-created-at-desc-performance.md**](report/product-list-created-at-desc-performance.md)

  - **상품 목록 조회 - 가격 기준 정렬** 성능 최적화

    [**product-list-price-sort-performance.md**](report/product-list-price-sort-performance.md)


## **⚙️ 실행 방법**

### 1. **DB 및 초기 데이터 실행**

```bash
bash
CopyEdit
# 전체 MySQL 초기화 + 스키마 및 데이터 자동 적용
./init/reset-db.sh

```

- MySQL 컨테이너를 완전히 초기화하고, `init/01-schema.sql`과 `init/02-data.sql`을 실행하여 필요한 스키마 및 데이터를 설정합니다.
- `./data/mysql` 디렉토리도 초기화되어, 테스트가 완전한 새 상태에서 실행됩니다.

### 2. **테스트 실행 방법**

```bash
bash
CopyEdit
./gradlew test

```

- **Testcontainers**를 기반으로 한 통합 테스트가 실행됩니다.
- `application.yml`에 DB 정보를 따로 설정할 필요 없이, 자동으로 테스트용 DB 컨테이너가 실행됩니다.
- 각 테스트는 `@Transactional`을 사용하며 테스트용 DB에서 실행되므로 **안정적이고 격리된 테스트**가 가능합니다.

---
