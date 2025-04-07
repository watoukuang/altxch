package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStateEnum {
    TRADING("TRADING", "交易中"),

    COMPLETED("COMPLETED", "已完成"),

    CANCELED("CANCELED", "已取消"),

    OVER_TIMED("OVER_TIMED", "超时");

    private final String key;
    private final String description;
}
