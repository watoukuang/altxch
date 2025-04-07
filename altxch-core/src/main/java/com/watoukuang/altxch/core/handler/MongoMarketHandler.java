package com.watoukuang.altxch.core.handler;

import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoMarketHandler implements MarketHandler {
    private final MongoTemplate mongoTemplate;

    @Override
    public void handleTrade(String symbol, ExchangeTrade exchangeTrade, CoinThumb thumb) {
        mongoTemplate.insert(exchangeTrade, "exchange_trade_" + symbol);
    }

    @Override
    public void handleKLine(String symbol, KLine kLine) {
        mongoTemplate.insert(kLine, "exchange_kline_" + symbol + "_" + kLine.getPeriod());
    }
}
