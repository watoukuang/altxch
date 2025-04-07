package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单方向枚举类
 * 包含买入和卖出的订单方向信息。
 */
@Getter
@AllArgsConstructor
public enum OrderDirectionEnum {
    /**
     * 买入订单
     */
    BUY("BUY", "买"),
    /**
     * 卖出订单
     */
    SELL("SELL", "卖");

    private final String key;
    private final String description;
}
