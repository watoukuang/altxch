package com.watoukuang.altxch.match.config;

import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import com.watoukuang.altxch.match.trader.CoinTrader;
import com.watoukuang.altxch.match.trader.CoinTraderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Slf4j
@Configuration
public class CoinTraderConfig {

    @Bean
    public CoinTraderFactory getCoinTrader(ExchangeCoinService exchangeCoinService, KafkaTemplate<String, String> kafkaTemplate) {
        CoinTraderFactory factory = new CoinTraderFactory();
        List<ExchangeCoin> coins = exchangeCoinService.findAllEnabled();
        if (coins == null || coins.isEmpty()) {
            return factory;
        }
        for (ExchangeCoin coin : coins) {
            log.info("init trader,symbol={}", coin.getSymbol());
            CoinTrader trader = new CoinTrader(coin.getSymbol());
            trader.setKafkaTemplate(kafkaTemplate);
            trader.setBaseCoinScale(coin.getBaseCoinScale());
            trader.setCoinScale(coin.getCoinScale());
            trader.setPublishType(coin.getPublishType());
            trader.setClearTime(coin.getClearTime());
            trader.stopTrading();
            factory.addTrader(coin.getSymbol(), trader);
        }
        return factory;
    }
}
