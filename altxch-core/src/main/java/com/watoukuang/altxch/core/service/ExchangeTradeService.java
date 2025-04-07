package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.core.domain.ExchangeTrade;

import java.util.List;

public interface ExchangeTradeService {
    List<ExchangeTrade> listLatestTrades(String symbol, int size);
}
