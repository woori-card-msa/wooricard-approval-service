# Wooricard Approval Service

## 서비스 개요

카드사 승인 시스템의 핵심 서비스로, VAN으로부터 카드 승인 요청을 수신하여 처리합니다.

- 체크카드: 은행 서비스에 잔액 조회 및 출금 요청
- 신용카드: 신용 한도 확인 및 차감
- 카드 유효성 검증 (Luhn 알고리즘, 상태, 유효기간, PIN)
- 승인/거절/취소 내역 저장 및 조회
- 청구 서비스에 월별 승인 내역 제공

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.12 |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security 6, BCrypt |
| DB | MySQL 8 |
| Service Discovery | Spring Cloud Eureka Client |
| API Docs | SpringDoc OpenAPI (Swagger) 2.3.0 |
| Build | Gradle |

---

## 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                        VAN / Client                         │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP
                      ▼
┌─────────────────────────────────────────────────────────────┐
│               Wooricard Approval Service (:8081)            │
│                                                             │
│  Controller → Service → Repository → MySQL DB              │
│                  │                                          │
│                  └──────────────────────────────────────┐  │
│                                                          │  │
└──────────────────────────────────────────────────────────┼──┘
                                                           │ HTTP
                                                           ▼
                                              ┌────────────────────┐
                                              │  Bank Service       │
                                              │  (:8080)            │
                                              │  잔액조회 / 출금    │
                                              │  환불               │
                                              └────────────────────┘
```

**요청 흐름**

```
승인 요청
  → 중복 거래 체크
  → 카드 유효성 검증 (Luhn, 상태, 유효기간, PIN)
  → 카드 타입 분기
      ├─ 체크카드: 은행 잔액 조회 → 출금 요청
      └─ 신용카드: 한도 확인 (비관적 락) → 한도 차감
  → 승인/거절 내역 저장
  → 응답 반환
```

---

## 역할

| 레이어 | 클래스 | 역할 |
|--------|--------|------|
| Controller | `AuthorizationController` | 승인 요청/취소/조회 API |
| Controller | `BillingController` | 월별 승인 내역 조회 API (청구 서비스용) |
| Controller | `HealthController` | 헬스 체크 |
| Service | `AuthorizationService` | 승인/취소/조회 비즈니스 로직 |
| Service | `CardValidationService` | 카드 유효성 검증 (Luhn, 상태, 유효기간, PIN) |
| Client | `BankClient` | 은행 서비스 연동 (잔액 조회, 출금, 환불) |
| Repository | `AuthorizationRepository` | 승인 내역 DB 접근 |
| Repository | `CardRepository` | 카드 정보 DB 접근 |
| Config | `SecurityConfig` | Spring Security 설정 |
| Config | `OpenApiConfig` | Swagger 설정 |

---

## 프로젝트 구조

```
src/main/java/com/wooricard/approval/
├── WooricardApprovalServiceApplication.java
├── controller/
│   ├── AuthorizationController.java
│   ├── BillingController.java
│   └── HealthController.java
├── service/
│   ├── AuthorizationService.java
│   └── CardValidationService.java
├── repository/
│   ├── AuthorizationRepository.java
│   └── CardRepository.java
├── entity/
│   ├── Authorization.java
│   ├── AuthorizationStatus.java   (APPROVED, REJECTED, CONFIRMED, CANCELLED)
│   ├── Card.java
│   ├── CardStatus.java            (ACTIVE, SUSPENDED, LOST, TERMINATED)
│   └── CardType.java              (CREDIT, DEBIT)
├── dto/
│   ├── AuthorizationRequest.java
│   ├── AuthorizationResponse.java
│   ├── CancelRequest.java
│   ├── CancelResponse.java
│   ├── ApprovalDto.java
│   ├── MonthlyApprovalDto.java
│   ├── BalanceRequest/Response.java
│   ├── DebitRequest/Response.java
│   └── RefundRequest/Response.java
├── client/
│   ├── BankClient.java
│   └── BankClientImpl.java
└── config/
    ├── SecurityConfig.java
    └── OpenApiConfig.java

src/main/resources/
├── application.yml
└── data.sql
```

---

## DB 설계

### cards

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT (PK) | 카드 고유 번호 |
| card_number | VARCHAR(500) | 카드 번호 (암호화) |
| card_type | VARCHAR(20) | 카드 타입 (CREDIT / DEBIT) |
| card_status | VARCHAR(20) | 카드 상태 (ACTIVE / SUSPENDED / LOST / TERMINATED) |
| expiry_date | DATE | 유효기간 |
| credit_limit | DECIMAL(15,2) | 신용 한도 (신용카드만) |
| used_amount | DECIMAL(15,2) | 사용 금액 (신용카드만) |
| pin | VARCHAR(500) | PIN (BCrypt 암호화) |
| customer_id | VARCHAR(50) | 고객 ID |
| created_at | DATETIME | 생성 일시 |
| version | BIGINT | 낙관적 락 버전 |

### authorizations

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT (PK) | 승인 고유 번호 |
| transaction_id | VARCHAR(100) UNIQUE | 거래 고유 번호 |
| card_number_masked | VARCHAR(19) | 마스킹된 카드번호 (예: 6011-\*\*\*\*-\*\*\*\*-1117) |
| amount | DECIMAL(15,2) | 결제 금액 |
| merchant_id | VARCHAR(50) | 가맹점 ID |
| approval_number | VARCHAR(8) | 승인 번호 |
| response_code | VARCHAR(2) | 응답 코드 |
| status | VARCHAR(20) | 승인 상태 (APPROVED / REJECTED / CONFIRMED / CANCELLED) |
| authorization_date | DATETIME | 승인 일시 |
| created_at | DATETIME | 생성 일시 |

### 관계

```
cards (1) ──── (N) authorizations
  card_number         card_number_masked (논리적 연관, FK 없음)
```

---

## API 명세

### 카드 승인

```
POST /api/authorizations/request
```

| 필드 | 타입 | 설명 |
|------|------|------|
| transactionId | String | 거래 고유 번호 |
| cardNumber | String | 카드 번호 |
| amount | BigDecimal | 결제 금액 |
| merchantId | String | 가맹점 ID |
| pin | String | PIN 번호 |

### 승인 취소

```
POST /api/authorizations/cancel
```

| 필드 | 타입 | 설명 |
|------|------|------|
| transactionId | String | 취소할 거래 번호 |
| cardNumber | String | 카드 번호 |

### 승인 내역 조회 (날짜 기준)

```
GET /api/authorizations?date=2026-03-20&page=0&size=100&status=APPROVED
```

### 월별 승인 내역 조회

```
GET /api/authorization/approved/monthly?cardNumberMasked=6011-****-****-1117&month=2026-03
```

---

## 응답 코드

| 코드 | 설명 |
|------|------|
| 00 | 승인 |
| 14 | 카드 정지 / 분실 / 해지 |
| 25 | 원거래 없음 |
| 51 | 잔액 부족 |
| 54 | 유효기간 만료 |
| 55 | PIN 오류 |
| 58 | 매출 확정된 거래 취소 불가 |
| 61 | 한도 초과 |
| 94 | 중복 거래 |
| 96 | 시스템 오류 |

---

## 실행 방법

### 1. DB 생성

```sql
CREATE DATABASE card_authorization_db CHARACTER SET utf8mb4;
```

### 2. 실행

```bash
./gradlew bootRun
```

- **서버 포트:** 8081
- **Swagger UI:** http://localhost:8081/swagger-ui.html

---

## 테스트 카드

| 카드번호 | 종류 | 상태 | PIN | 비고 |
|----------|------|------|-----|------|
| 4111111111111111 | 체크 | 정상 | 1234 | |
| 5555555555554444 | 체크 | 정상 | 1234 | 잔액 부족 테스트용 |
| 6011111111111117 | 신용 | 정상 | 1234 | 한도 500만원 |
| 3530111333300000 | 신용 | 정상 | 1234 | 잔여 한도 50만원 |
| 5105105105105100 | 체크 | 정지 | 1234 | |
| 4012888888881881 | 체크 | 정상 | 1234 | 유효기간 만료 |
| 7011111111111117 | 신용 | 정상 | 1234 | 한도 500만원 |
| 8011111111111117 | 신용 | 정상 | 1234 | 한도 500만원 |
