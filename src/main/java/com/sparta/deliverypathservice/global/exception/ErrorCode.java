package com.sparta.deliverypathservice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DELIVERY_PATH_ERROR(8000, HttpStatus.INTERNAL_SERVER_ERROR, "배송 경로 에러 발생"),
    DELIVERY_PATH_FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, "배송 경로: 권한 없음"),
    ;

    private final int code;
    private final HttpStatus status;
    private final String details;
}
