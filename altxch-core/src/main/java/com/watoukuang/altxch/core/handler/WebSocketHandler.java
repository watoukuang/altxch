package com.watoukuang.altxch.core.handler;

import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;
import com.watoukuang.altxch.core.service.ExchangePushService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketHandler implements MarketHandler {
    private final ExchangePushService exchangePushService;
//    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void handleTrade(String symbol, ExchangeTrade exchangeTrade, CoinThumb thumb) {
        //推送缩略行情
        exchangePushService.addThumb(symbol, thumb);
    }

    @Override
    public void handleKLine(String symbol, KLine kLine) {
        //推送K线数据
//        messagingTemplate.convertAndSend("/topic/altxch-market/kline/" + symbol, kLine);
    }
}
