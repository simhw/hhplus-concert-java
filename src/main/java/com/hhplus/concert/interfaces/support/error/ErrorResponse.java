package com.hhplus.concert.interfaces.support.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String status;
    private String message;
    private Object payload;
}
