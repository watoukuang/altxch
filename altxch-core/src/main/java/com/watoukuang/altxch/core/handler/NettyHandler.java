package com.watoukuang.altxch.core.handler;

import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;
import org.springframework.stereotype.Service;

/**
 * 处理NETTY订阅与取消订阅
 */
@Service
public class NettyHandler implements MarketHandler {
    @Override
    public void handleTrade(String symbol, ExchangeTrade exchangeTrade, CoinThumb thumb) {

    }

    @Override
    public void handleKLine(String symbol, KLine kLine) {

    }
}
