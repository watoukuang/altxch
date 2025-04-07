package com.watoukuang.altxch.core.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradePlateItem {

    private BigDecimal price;

    private BigDecimal amount;
}
