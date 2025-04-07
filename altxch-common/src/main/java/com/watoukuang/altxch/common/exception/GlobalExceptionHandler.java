package com.watoukuang.altxch.common.exception;

import com.watoukuang.altxch.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 服务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> serviceException(ServiceException e) {
        log.error("服务异常: {}", e.getMessage(), e);
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("服务器内部错误: {}", e.getMessage(), e);
        return R.fail("服务器内部错误!");
    }
}
