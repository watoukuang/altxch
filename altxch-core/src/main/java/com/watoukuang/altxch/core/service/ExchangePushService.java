package com.watoukuang.altxch.core.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.TradePlate;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class ExchangePushService {
    @Getter
    private final ConcurrentHashMap<String, List<ExchangeTrade>> tradesQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<String, List<CoinThumb>> thumbQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<TradePlate>> plateQueue = new ConcurrentHashMap<>();
    @Getter
    private ConcurrentHashMap<String, TradePlate> plateLastBuy = new ConcurrentHashMap<>();
    @Getter
    private ConcurrentHashMap<String, TradePlate> plateLastBuyOrigin = new ConcurrentHashMap<>();
    @Getter
    private ConcurrentHashMap<String, TradePlate> plateLastSell = new ConcurrentHashMap<>();
    @Getter
    private ConcurrentHashMap<String, TradePlate> plateLastSellOrigin = new ConcurrentHashMap<>();

    private BigDecimal lastBuyHeightPrice = BigDecimal.ZERO;
    private BigDecimal lastSellLowPrice = BigDecimal.ZERO;

    @Getter
    private final ConcurrentHashMap<String, CoinThumb> lastPushThumb = new ConcurrentHashMap<>();


    public void addTrades(String symbol, List<ExchangeTrade> trades) {
        List<ExchangeTrade> exchangeTrades = tradesQueue.get(symbol);
        if (CollectionUtils.isEmpty(exchangeTrades)) {
            exchangeTrades = new CopyOnWriteArrayList<>();
            tradesQueue.put(symbol, exchangeTrades);
        }
        synchronized (exchangeTrades) {
            exchangeTrades.addAll(trades);
        }
        log.info("ExchangePushService addTrades tradesQueue:{}", JSON.toJSONString(tradesQueue));
    }

    public void addThumb(String symbol, CoinThumb thumb) {
        List<CoinThumb> coinThumbs = thumbQueue.get(symbol);
        if (CollectionUtils.isEmpty(coinThumbs)) {
            coinThumbs = new CopyOnWriteArrayList<>();
            thumbQueue.put(symbol, coinThumbs);
        }
        synchronized (coinThumbs) {
            coinThumbs.add(thumb);
        }
        log.info("ExchangePushService addThumb coinThumbs:{}", JSON.toJSONString(coinThumbs));
    }

    public void addPlates(String symbol, TradePlate plate) {
        CopyOnWriteArrayList<TradePlate> tradePlates = plateQueue.get(symbol);
        if (CollectionUtils.isEmpty(tradePlates)) {
            tradePlates = new CopyOnWriteArrayList<>();
            plateQueue.put(symbol, tradePlates);
        }
        synchronized (tradePlates) {
            tradePlates.add(plate);
        }
        // 更新最新盘口
        String direction = plate.getDirection();
        if (OrderDirectionEnum.BUY.getKey().equals(direction)) {
            updateLastBuyPlate(symbol, plate);
        } else if (OrderDirectionEnum.SELL.getKey().equals(direction)) {
            updateLastSellPlate(symbol, plate);
        }
        log.info("ExchangePushService addPlates plateQueue:{}", JSON.toJSONString(plateQueue));
    }


    private void updateLastBuyPlate(String symbol, TradePlate plate) {
        // 使用 synchronized 块确保线程安全
        synchronized (plateLastBuy) {
            plateLastBuy.put(symbol, plate);
            plateLastBuyOrigin = new ConcurrentHashMap<>(plateLastBuy); // 使用 HashMap 的构造函数复制
            lastBuyHeightPrice = plate.getHighestPrice();
        }
    }

    private void updateLastSellPlate(String symbol, TradePlate plate) {
        // 使用 synchronized 块确保线程安全
        synchronized (plateLastSell) {
            plateLastSell.put(symbol, plate);
            plateLastSellOrigin = new ConcurrentHashMap<>(plateLastSell); // 使用 HashMap 的构造函数复制
            lastSellLowPrice = plate.getLowestPrice();
        }
    }

    public void resetLastBuyAndSell(String symbol) {
        plateLastBuy = new ConcurrentHashMap<>(plateLastBuyOrigin);
        plateLastSell = new ConcurrentHashMap<>(plateLastSellOrigin);
    }


}
