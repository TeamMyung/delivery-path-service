package com.sparta.deliverypathservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeliveryPathException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String description;
}
