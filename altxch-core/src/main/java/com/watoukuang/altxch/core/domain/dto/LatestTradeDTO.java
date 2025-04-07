package com.watoukuang.altxch.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LatestTradeDTO implements Serializable {
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 数据大小
     */
    private Integer size;
}
