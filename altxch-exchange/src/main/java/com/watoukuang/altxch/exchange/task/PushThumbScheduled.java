package com.watoukuang.altxch.exchange.task;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.service.ExchangePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushThumbScheduled {
    private final ExchangePushService exchangePushService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 1000)
    public void run() {
        Map<String, List<CoinThumb>> thumbQueue = exchangePushService.getThumbQueue();
        System.out.println(JSON.toJSONString(thumbQueue));
        for (Map.Entry<String, List<CoinThumb>> entry : thumbQueue.entrySet()) {
            String symbol = entry.getKey();
            List<CoinThumb> thumbs = entry.getValue();
            if (!thumbs.isEmpty()) {
                synchronized (thumbs) {
                    CoinThumb pushThumb = thumbs.get(thumbs.size() - 1);
                    ConcurrentHashMap<String, CoinThumb> lastPushThumb = exchangePushService.getLastPushThumb();
                    if (lastPushThumb.get(symbol) != null && lastPushThumb.get(symbol).getVolume().compareTo(pushThumb.getVolume()) > 0) {
                        pushThumb.setVolume(lastPushThumb.get(symbol).getVolume());
                    }
                    pushThumb.setTime(System.currentTimeMillis());
                    messagingTemplate.convertAndSend("/topic/altxch-market/thumb", thumbs.get(thumbs.size() - 1));
                    lastPushThumb.put(symbol, thumbs.get(thumbs.size() - 1));
                    thumbs.clear();
                }
            }
        }
    }
}
