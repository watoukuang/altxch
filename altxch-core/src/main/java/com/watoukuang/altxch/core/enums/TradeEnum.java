package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeEnum {

    /**
     * 可交易
     */
    Y(1, "可交易"),

    /**
     * 不可交易
     */
    N(2, "不可交易");

    private final int code;

    private final String description;
}
