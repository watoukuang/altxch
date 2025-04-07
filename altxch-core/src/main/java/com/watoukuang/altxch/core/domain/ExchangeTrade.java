package com.watoukuang.altxch.core.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 撮合交易信息
 */
@Data
public class ExchangeTrade implements Serializable {
    /**
     * 交易对的标识符，例如 "BTC/USD"。
     */
    private String symbol;

    /**
     * 交易价格，表示每个单位的成交价格。
     */
    private BigDecimal price;

    /**
     * 交易数量，表示成交的总量。
     */
    private BigDecimal amount;

    /**
     * 买入成交的总金额，计算公式为买入价格 * 买入数量。
     */
    private BigDecimal buyTurnover;

    /**
     * 卖出成交的总金额，计算公式为卖出价格 * 卖出数量。
     */
    private BigDecimal sellTurnover;

    /**
     * 交易方向，表示是买入还是卖出。
     */
    private String direction;

    /**
     * 买入订单的唯一标识符，用于追踪买入订单。
     */
    private String buyOrderId;

    /**
     * 卖出订单的唯一标识符，用于追踪卖出订单。
     */
    private String sellOrderId;

    /**
     * 成交时间，表示交易发生的时间戳（以毫秒为单位）。
     */
    private Long time;
}
