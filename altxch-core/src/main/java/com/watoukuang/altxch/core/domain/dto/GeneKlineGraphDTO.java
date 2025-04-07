package com.watoukuang.altxch.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeneKlineGraphDTO implements Serializable {
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * K 线数据的时间间隔（分辨率），例如 "1m"、"5m"、"1h"、"1D"。
     */
    private String resolution;

    /**
     * 交易对
     */
    private String symbol;
}
