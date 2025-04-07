package com.watoukuang.altxch.exchange.config;

import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.CoinExchangeRate;
import com.watoukuang.altxch.core.handler.MongoMarketHandler;
import com.watoukuang.altxch.core.handler.NettyHandler;
import com.watoukuang.altxch.core.handler.WebSocketHandler;
import com.watoukuang.altxch.core.processor.CoinProcessor;
import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import com.watoukuang.altxch.core.processor.DefaultCoinProcessor;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import com.watoukuang.altxch.core.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CoinProcessorConfig {
    @Bean
    public CoinProcessorFactory processorFactory(MongoMarketHandler mongoMarketHandler, WebSocketHandler webSocketHandler,
                                                 NettyHandler nettyHandler, MarketService marketService,
                                                 CoinExchangeRate coinExchangeRate, ExchangeCoinService exchangeCoinService) {
        CoinProcessorFactory factory = new CoinProcessorFactory();
        List<ExchangeCoin> coins = exchangeCoinService.findAllEnabled();

        for (ExchangeCoin coin : coins) {
            CoinProcessor processor = new DefaultCoinProcessor(coin.getSymbol(), coin.getBaseSymbol());
            processor.addHandler(mongoMarketHandler);
            processor.addHandler(webSocketHandler);
            processor.addHandler(nettyHandler);
            processor.setMarketService(marketService);
            processor.setCoinExchangeRate(coinExchangeRate);
            processor.setIsStopKLine(true);
            factory.addProcessor(coin.getSymbol(), processor);
        }
        coinExchangeRate.setCoinProcessorFactory(factory);
        return factory;
    }
}
