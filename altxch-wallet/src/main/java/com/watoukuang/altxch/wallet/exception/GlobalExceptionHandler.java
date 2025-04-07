package com.watoukuang.altxch.wallet.exception;

import com.watoukuang.altxch.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("Internal server error:{}", e.getMessage());
        return R.fail("Internal server error");
    }

    @ExceptionHandler(ServiceException.class)
    public R<Void> handleException(ServiceException e) {
        log.error("Internal server error:{}", e.getMessage());
        return R.fail("Internal server error");
    }
}
