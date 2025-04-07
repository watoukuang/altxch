package com.watoukuang.altxch.core.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinThumb {
    /**
     * 交易对符号(例如 BTC/USD)
     */
    private String symbol;

    /**
     * 数据的时间戳，表示该数据的时间
     */
    private Long time;

    /**
     * 开盘价，初始化为零
     */
    private BigDecimal open = BigDecimal.ZERO;

    /**
     * 最高价，初始化为零
     */
    private BigDecimal high = BigDecimal.ZERO;

    /**
     * 最低价，初始化为零
     */
    private BigDecimal low = BigDecimal.ZERO;

    /**
     * 收盘价，初始化为零
     */
    private BigDecimal close = BigDecimal.ZERO;

    /**
     * 价格变化，初始化为零，精确到小数点后两位
     */
    private BigDecimal chg = BigDecimal.ZERO.setScale(2);

    /**
     * 价格变化百分比，初始化为零，精确到小数点后两位
     */
    private BigDecimal change = BigDecimal.ZERO.setScale(2);

    /**
     * 交易量，初始化为零，精确到小数点后两位
     */
    private BigDecimal volume = BigDecimal.ZERO.setScale(2);

    /**
     * 总成交额，初始化为零
     */
    private BigDecimal turnover = BigDecimal.ZERO;

    /**
     * 昨日收盘价，初始化为零
     */
    private BigDecimal lastDayClose = BigDecimal.ZERO;

    /**
     * 交易币对美元的汇率
     */
    private BigDecimal usdRate;

    /**
     * 基币对美元的汇率
     */
    private BigDecimal baseUsdRate;

    /**
     * 交易区，例如地区或市场编号
     */
    private int zone;
}