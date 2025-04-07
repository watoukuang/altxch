package com.watoukuang.altxch.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class KLineGraphDTO implements Serializable {
    /**
     * 交易对的符号，例如 "BTC/USDT"。
     */
    private String symbol;

    /**
     * 请求的 K 线数据的起始时间，以毫秒为单位（自 Unix 纪元以来）。
     */
    private Long from;

    /**
     * 请求的 K 线数据的结束时间，以毫秒为单位（自 Unix 纪元以来）。
     */
    private Long to;

    /**
     * K 线数据的时间间隔（分辨率），例如 "1m"、"5m"、"1h"、"1D"。
     */
    private String resolution;
}
