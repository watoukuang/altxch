package com.watoukuang.altxch.exchange.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    /**
     * 订单方向
     */
    private String direction;

    /**
     * 订单类型:(市价单、限价单)
     */
    private String orderType;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 价格
     */
    private BigDecimal price;
}
