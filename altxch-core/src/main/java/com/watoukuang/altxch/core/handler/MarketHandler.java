package com.watoukuang.altxch.core.handler;

import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;

public interface MarketHandler {
    /**
     * 存储交易信息
     */
    void handleTrade(String symbol, ExchangeTrade exchangeTrade, CoinThumb thumb);


    /**
     * 存储K线信息
     */
    void handleKLine(String symbol, KLine kLine);
}
