package com.watoukuang.altxch.exchange.task;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.service.ExchangePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushTradeScheduled {
    private final ExchangePushService exchangePushService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 800)
    public void run() {
        ConcurrentHashMap<String, List<ExchangeTrade>> tradesQueue = exchangePushService.getTradesQueue();
        for (Map.Entry<String, List<ExchangeTrade>> entry : tradesQueue.entrySet()) {
            String symbol = entry.getKey();
            List<ExchangeTrade> trades = entry.getValue();
            if (!trades.isEmpty()) {
                synchronized (trades) {
                    log.info("ExchangePushServiceImpl.pushTrade trades:{}", JSON.toJSONString(trades));
                    messagingTemplate.convertAndSend("/topic/altxch-market/trade/" + symbol, trades);
                    trades.clear();
                }
            }
        }
    }
}
