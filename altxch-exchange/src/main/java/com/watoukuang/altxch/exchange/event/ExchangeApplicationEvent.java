package com.watoukuang.altxch.exchange.event;

import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.processor.CoinProcessor;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeApplicationEvent implements ApplicationListener<ContextRefreshedEvent> {
    private final ExchangeCoinService exchangeCoinService;
    private final CoinProcessorFactory coinProcessorFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("====应用初始化完成，开启CoinProcessor====");
        List<ExchangeCoin> coins = exchangeCoinService.findAllEnabled();
        coins.forEach(coin -> {
            CoinProcessor processor = coinProcessorFactory.getProcessor(coin.getSymbol());
            processor.initializeThumb();
            processor.initializeUsdRate();
            processor.setIsHalt(false);
        });
    }
}
