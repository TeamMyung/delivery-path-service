package com.sparta.deliverypathservice.global.exception;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DeliveryPathExceptionHandler implements Ordered {

    @ExceptionHandler(DeliveryPathException.class)
    public ResponseEntity<ApiResponse<Object>> handle(DeliveryPathException exception) {

        log.error(exception.getDescription(), exception);

        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(new ApiResponse<>(exception.getErrorCode()));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
