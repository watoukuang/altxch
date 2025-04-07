package com.watoukuang.altxch.core.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * KLine 类表示 K 线图中的一根 K 线数据
 */
@Data
public class KLine {
    /**
     * K线的时间段，例如 "1分钟"、"5分钟" 等
     */
    private String period;
    /**
     * 开盘价，K线开始时的价格
     */
    private BigDecimal openPrice = BigDecimal.ZERO;

    /**
     * 最高价，K 线期间内的最高价格
     */
    private BigDecimal highestPrice = BigDecimal.ZERO;
    /**
     * 最低价，K 线期间内的最低价格
     */
    private BigDecimal lowestPrice = BigDecimal.ZERO;

    /**
     * 收盘价，K 线结束时的价格
     */
    private BigDecimal closePrice = BigDecimal.ZERO;

    /**
     * 时间，表示该 K 线的时间戳
     */
    private long time;

    /**
     * 成交笔数
     */
    private int count;
    /**
     * 成交量
     */
    private BigDecimal volume = BigDecimal.ZERO;
    /**
     * 成交额
     */
    private BigDecimal turnover = BigDecimal.ZERO;

    public KLine() {

    }

    public KLine(String period) {
        this.period = period;
    }


    // 构造函数和 getter 方法
    public KLine(long time, BigDecimal openPrice, BigDecimal highestPrice, BigDecimal lowestPrice, BigDecimal closePrice, BigDecimal volume) {
        this.time = time;
        this.openPrice = openPrice;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }
}
