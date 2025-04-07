package com.watoukuang.altxch.exchange.task;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.common.mybatis.query.LambdaQueryWrapperPlus;
import com.watoukuang.altxch.common.utils.GeneratorUtil;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.dao.mapper.ExchangeCoinMapper;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.TradePlate;
import com.watoukuang.altxch.core.domain.TradePlateItem;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import com.watoukuang.altxch.core.service.ExchangePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushPlateScheduled {

    private final ExchangeCoinMapper exchangeCoinMapper;
    private final Random rand = new Random();

    private final ExchangePushService exchangePushService;

    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.info("PushPlateScheduled start run...");
        // 遍历每个交易对的盘口队列
        ConcurrentHashMap<String, CopyOnWriteArrayList<TradePlate>> plateQueue = exchangePushService.getPlateQueue();
        plateQueue.forEach((symbol, plates) -> {
            Optional.ofNullable(plates).filter(list -> !list.isEmpty()).map(nonEmptyPlates -> {
                handleRealPlates(nonEmptyPlates, symbol);
                return true;
            });
        });
        log.info("PushPlateScheduled stop...");
    }

    private void handleRealPlates(CopyOnWriteArrayList<TradePlate> plates, String symbol) {
        log.info("PushPlateScheduled.handleRealPlates symbol:{}", symbol);
        // 用于跟踪已推送的方向
        Set<String> pushedDirections = new HashSet<>();
        plates.stream().filter(plate -> {
            String direction = plate.getDirection();
            // 检查方向是否已经推送
            return pushedDirections.add(direction);
        }).forEach(plate -> pushPlateMessage(symbol, plate));
        // 清空已处理的盘口列表
        plates.clear();
    }

    private void pushPlateMessage(String symbol, TradePlate plate) {
        log.info("ExchangePushServiceImpl.pushTrade trades:{}", JSON.toJSONString(plate.toJSON(24)));
        // WebSocket 推送盘口信息
        messagingTemplate.convertAndSend("/topic/altxch-market/trade-plate/" + symbol, plate.toJSON(24));
        // WebSocket 推送深度信息
        messagingTemplate.convertAndSend("/topic/altxch-market/trade-depth/" + symbol, plate.toJSON(50));
    }


}
