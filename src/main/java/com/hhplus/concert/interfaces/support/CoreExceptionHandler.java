package com.hhplus.concert.interfaces.support;

import com.hhplus.concert.domain.support.error.CoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CoreExceptionHandler {
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handle(CoreException e) {
        switch (e.getErrorType().getLevel()) {
            case ERROR:
                log.error(e.getMessage(), e);
                break;
            case WARN:
                log.warn(e.getMessage(), e);
                break;
            default:
                log.info(e.getMessage());
                break;
        }

        HttpStatus status = switch (e.getErrorType().getErrorCode()) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;    // 400
            case RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;    // 404
            default -> HttpStatus.OK;  // 200
        };

        ErrorResponse error = new ErrorResponse(
                e.getErrorType().toString(),
                "FAIL",
                e.getMessage(),
                e.getPayload()
        );
        return new ResponseEntity<>(error, status);
    }
}
