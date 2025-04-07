package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务订单类型
 */
@Getter
@AllArgsConstructor
public enum BizOrderTypeEnum {
    /**
     * 限价单
     */
    OTC("OTC", "场外"),

    /**
     * 市价单
     */
    EXCHANGE("EXCHANGE", "交易所");


    private final String key;
    private final String description;
}
