package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举类
 * 包含不同类型的订单（市价单和限价单）的信息。
 */
@Getter
@AllArgsConstructor
public enum ExchangeOrderTypeEnum {
    /**
     * 限价单
     */
    LIMIT_PRICE("LIMIT_PRICE", "限价单"),
    /**
     * 市价单
     */
    MARKET_PRICE("MARKET_PRICE", "市价单");


    private final String key;
    private final String description;
}
