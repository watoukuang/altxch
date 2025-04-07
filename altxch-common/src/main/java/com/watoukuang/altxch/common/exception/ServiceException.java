package com.watoukuang.altxch.common.exception;

import com.watoukuang.altxch.common.response.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    public ServiceException(String message) {
        super(message);
        this.code = Code.FAIL;
        this.message = message;
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Throwable e) {
        super(message, e);
        this.code = Code.FAIL;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
