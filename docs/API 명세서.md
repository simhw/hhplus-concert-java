# API 명세서

## 1. 대기열 등록 API

---

### [POST] /api/queue

- 접속 대기열에 사용자를 등록합니다.

### Request Headers

| 헤더 이름          | 값                  | 설명                   |
|----------------|--------------------|----------------------|
| `Content-Type` | `application/json` | 요청 본문의 데이터 형식        |
| `Accept`       | `application/json` | 서버로부터 JSON 형식의 응답 기대 |

### Request Body

```json
{
  "userId": 1
}
```

| 필드명      | 타입     | 설명       |
|----------|--------|----------|
| `userId` | `Long` | 사용자  아이디 |

### Response Body

```json
{
  "code": "QUEUE_CODE"
}
```

| 필드명    | 타입       | 설명                      |
|--------|----------|-------------------------|
| `code` | `String` | API 접근을 위한 대기열 상태 인증 코드 |

## 2. 대기열 상태 조회 API

---

### [GET] /api/queue/status

- 접속 대기열의 상태를 조회합니다.

### Request Headers

| 헤더 이름        | 값                  | 설명                      |
|--------------|--------------------|-------------------------|
| `Queue-Code` | `QUEUE_CODE`       | API 접근을 위한 대기열 상태 인증 코드 |
| `Accept`     | `application/json` | 서버로부터 JSON 형식의 응답 기대    |

### Response Body

```json
{
  "code": "QUEUE_CODE",
  "status": "WAIT",
  "number": 10
}
```

| 필드명      | 타입        | 설명                      |
|----------|-----------|-------------------------|
| `code`   | `String`  | API 접근을 위한 대기열 상태 인증 코드 |
| `status` | `String`  | 대기열 상태                  |
| `number` | `Integer` | 대기 순서                   |

## 3. 콘서트 조회 API

---

### [GET] /api/concerts

- 콘서트 목록을 조회힙니다.

### Request Headers

| 헤더 이름    | 값                  | 설명                   |
|----------|--------------------|----------------------|
| `Accept` | `application/json` | 서버로부터 JSON 형식의 응답 기대 |

### Response Body

```json
{
  "id": 1,
  "title": "콘서드 제목"
}
```

| 필드명     | 타입       | 설명      |
|---------|----------|---------|
| `id`    | `Long`   | 콘서트 아이디 |
| `title` | `String` | 콘서트 제목  |

## 4. 콘서트 공연 조회 API

---

### [GET] /api/concerts/{concertId}

- 콘서트의 공연 정보와 예약 가능한 좌석, 불가능한 좌석 목록을 조회힙니다.

### Request Headers

| 헤더 이름        | 값                  | 설명                      |
|--------------|--------------------|-------------------------|
| `Queue-Code` | `QUEUE_CODE`       | API 접근을 위한 대기열 상태 인증 코드 |
| `Accept`     | `application/json` | 서버로부터 JSON 형식의 응답 기대    |

### Response Body

```json
[
  {
    "id": 1,
    "date": "2024-10-01",
    "startAt": "2024-10-01T10:00:00",
    "endAt": "2024-10-01T12:00:00",
    "availableSeats": [
      {
        "id": 1,
        "grade": "VIP",
        "no": 12,
        "price": 100000
      }
    ],
    "unavailableSeats": [
      {
        "id": 2,
        "grade": "VIP",
        "no": 13,
        "price": 100000
      }
    ]
  }
]
```

| 필드명                | 타입       | 설명                              |
|--------------------|----------|---------------------------------|
| `id`               | `Long`   | 콘서트 공연 아이디                      |
| `date`             | `String` | 콘서트 날짜 (yyyy-MM-dd)             |
| `startAt`          | `String` | 콘서트 시작 시간 (YYYY-MM-DDThh:mm:ss) |
| `endAt`            | `String` | 콘서트 종료 시간 (YYYY-MM-DDThh:mm:ss) |
| `endAt`            | `String` | 콘서트 종료 시간 (YYYY-MM-DDThh:mm:ss) |
| `availableSeats`   | `Object` | 예약 가능 좌석                        |
| `unavailableSeats` | `Object` | 예약 불가능 좌석                       |

| 필드명     | 타입        | 설명     |
|---------|-----------|--------|
| `id`    | `Long`    | 좌석 아이디 |
| `grade` | `String`  | 좌석 등급  |
| `no`    | `Integer` | 좌석 번호  |
| `price` | `Integer` | 가격     |

## 5. 콘서트 예약  API

---

### [POST] /api/reservations

- 콘서트의 공연 좌석을 예약합니다.

### Request Headers

| 헤더 이름        | 값                  | 설명                      |
|--------------|--------------------|-------------------------|
| `Queue-Code` | `QUEUE_CODE`       | API 접근을 위한 대기열 상태 인증 코드 |
| `Accept`     | `application/json` | 서버로부터 JSON 형식의 응답 기대    |

### Request Body

```json
{
  "userId": 1,
  "performanceId": 1,
  "seatId": 10
}
```

| 필드명             | 타입     | 설명          |
|-----------------|--------|-------------|
| `userId`        | `Long` | 사용자 아이디     |
| `performanceId` | `Long` | 콘서트 공연  아이디 |
| `seatId`        | `Long` | 공연 좌석  아이디  |

### Response Body

```json
{
  "id": 1,
  "status": "PAYMENT_WAIT"
}
```

| 필드명      | 타입       | 설명     |
|----------|----------|--------|
| `id`     | `Long`   | 예약 아이디 |
| `status` | `String` | 예약 상태  |

## 6. 예약 결제  API

---

### [POST] /api/payments

- 콘서트의 공연 좌석 예약건을 결제합니다.

### Request Headers

| 헤더 이름        | 값                  | 설명                      |
|--------------|--------------------|-------------------------|
| `Queue-Code` | `QUEUE_CODE`       | API 접근을 위한 대기열 상태 인증 코드 |
| `Accept`     | `application/json` | 서버로부터 JSON 형식의 응답 기대    |

### Request Body

```json
{
  "userId": 1,
  "reservationId": 1
}
```

| 필드명             | 타입     | 설명      |
|-----------------|--------|---------|
| `userId`        | `Long` | 사용자 아이디 |
| `reservationId` | `Long` | 예약 아이디  |

## 7. 잔액 조회  API

---

### [GET] /api/accounts/users/{userId}

- 계좌의 잔액을 조회합니다.

### Request Headers

| 헤더 이름    | 값                  | 설명                   |
|----------|--------------------|----------------------|
| `Accept` | `application/json` | 서버로부터 JSON 형식의 응답 기대 |

### Response Body

```json
{
  "userId": 1,
  "amount": 100000
}
```

| 필드명      | 타입        | 설명      |
|----------|-----------|---------|
| `userId` | `Long`    | 사용자 아이디 |
| `amount` | `Integer` | 계좌 잔액   |

## 8. 잔액 충전  API

---

### [PUT] /api/accounts/users/{userId}

- 계좌의 잔액을 충전합니다.

### Request Headers

| 헤더 이름    | 값                  | 설명                   |
|----------|--------------------|----------------------|
| `Accept` | `application/json` | 서버로부터 JSON 형식의 응답 기대 |

### Request Body

```json
{
  "userId": 1,
  "amount": 100000
}
```

| 필드명      | 타입        | 설명      |
|----------|-----------|---------|
| `userId` | `Long`    | 사용자 아이디 |
| `amount` | `Integer` | 충전 금액   |

### Response Body

```json
{
  "userId": 1,
  "amount": 100000
}
```

| 필드명      | 타입        | 설명      |
|----------|-----------|---------|
| `userId` | `Long`    | 사용자 아이디 |
| `amount` | `Integer` | 계좌 잔액   |