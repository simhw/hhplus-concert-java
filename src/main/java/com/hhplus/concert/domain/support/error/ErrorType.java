package com.hhplus.concert.domain.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // invalid
    // resource not found
    USER_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "사용자를 찾을 수 없습니다.", LogLevel.WARN),
    ACCOUNT_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "계좌를 찾을 수 없습니다.", LogLevel.WARN),
    QUEUE_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "대기 토큰을 찾을 수 없습니다.", LogLevel.WARN),
    CONCERT_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "콘서트를 찾을 수 없습니다.", LogLevel.WARN),
    CONCERT_PERFORMANCE_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "공연을 찾을 수 없습니다.", LogLevel.WARN),
    SEAT_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "좌석을 찾을 수 없습니다.", LogLevel.WARN),
    RESERVATION_NOT_FOUND(ErrorCode.RESOURCE_NOT_FOUND, "예약을 찾을 수 없습니다.", LogLevel.WARN),

    // business error
    PERFORMANCE_EXPIRED(ErrorCode.BUSINESS_ERROR, "예매가 불가능한 공연입니다.", LogLevel.INFO),
    DUPLICATED_RESERVATION(ErrorCode.BUSINESS_ERROR, "이미 예약된 좌석입니다.", LogLevel.INFO),
    MINIMUM_CHARGE_AMOUNT(ErrorCode.BUSINESS_ERROR, "1,000원 이상 충전이 가능합니다.", LogLevel.INFO),
    NOT_ENOUGH_ACCOUNT_AMOUNT(ErrorCode.BUSINESS_ERROR, "계좌 잔액이 부족합니다.", LogLevel.INFO),
    NOT_ACTIVE_QUEUE(ErrorCode.BUSINESS_ERROR, "아직 대기 상태입니다.", LogLevel.INFO),

    // server error
    FAIL_SAVE_QUEUE(ErrorCode.SERVER_ERROR, "대기열 등록에 실패했습니다.", LogLevel.WARN);

    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel level;
}
