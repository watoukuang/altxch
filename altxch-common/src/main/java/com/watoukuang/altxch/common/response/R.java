package com.watoukuang.altxch.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回前端统一响应
 */
@Getter
@Setter
public class R<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（无数据）
    public static <T> R<T> ok() {
        return new R<>(Code.OK, "OK", null);
    }

    // 成功响应（带数据）
    public static <T> R<T> ok(T data) {
        return new R<>(Code.OK, "OK", data);
    }

    // 成功响应（带消息和数据）
    public static <T> R<T> ok(String message, T data) {
        return new R<>(Code.OK, message, data);
    }

    // 成功响应（自定义状态码、消息和数据）
    public static <T> R<T> ok(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    // 失败响应（默认状态码和消息）
    public static R<Void> fail(String message) {
        return fail(Code.FAIL, message);
    }

    // 失败响应（自定义状态码，默认消息）
    public static R<Void> fail(int code) {
        return fail(code, "FAIL");
    }

    // 失败响应（自定义状态码和消息）
    public static R<Void> fail(int code, String message) {
        return fail(code, message, null);
    }

    // 失败响应（自定义状态码、消息和数据）
    public static <T> R<T> fail(int code, String message, T data) {
        return new R<>(code, message, data);
    }
}