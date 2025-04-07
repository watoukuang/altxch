package com.watoukuang.altxch.core.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 一个交易订单的详细信息
 */
@Data
public class ExchangeOrderDetail {
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单价格,表示每个单位的价格
     */
    private BigDecimal price;

    /**
     * 订单数量，表示交易的总量。
     */
    private BigDecimal amount;

    /**
     * 成交总金额，计算公式为 price * amount。
     */
    private BigDecimal turnover;

    /**
     * 交易手续费，表示此次交易的费用。
     */
    private BigDecimal fee;

    /**
     * 成交时间，表示订单成交的时间戳（以毫秒为单位）。
     */
    private long time;

}
