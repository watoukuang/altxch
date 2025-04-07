package com.watoukuang.altxch.exchange.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.TradePlate;
import com.watoukuang.altxch.core.processor.CoinProcessor;
import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import com.watoukuang.altxch.core.service.ExchangeOrderService;
import com.watoukuang.altxch.core.service.ExchangePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeTraderConsumer {
    private final ExchangeOrderService exchangeOrderService;
    private final CoinProcessorFactory coinProcessorFactory;
    @Value("${second.referrer.award}")
    private boolean secondReferrerAward;
    private final ExchangePushService exchangePushService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService executor = new ThreadPoolExecutor(30, 100, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());


    @KafkaListener(topics = "exchange-trade-plate", containerFactory = "kafkaListenerContainerFactory")
    public void handleTradePlate(List<ConsumerRecord<String, String>> records) {
        try {
            for (int i = 0; i < records.size(); i++) {
                ConsumerRecord<String, String> record = records.get(i);
                log.info("推送盘口信息topic={},value={},size={}", record.topic(), record.value(), records.size());
                TradePlate plate = JSON.parseObject(record.value(), TradePlate.class);
                String symbol = plate.getSymbol();
                exchangePushService.addPlates(symbol, plate);
            }
        } catch (Exception e) {
            log.error("处理盘口信息时发生错误: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "exchange-order-completed", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderCompleted(List<ConsumerRecord<String, String>> records) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                log.info("订单交易处理完成消息topic={},value={}", record.topic(), record.value());
                List<ExchangeOrder> orders = JSON.parseArray(record.value(), ExchangeOrder.class);
                for (ExchangeOrder order : orders) {
                    String symbol = order.getSymbol();
                    log.info("订单完成：{}", order);
                    // 委托成交完成处理
                    exchangeOrderService.tradeCompleted(order.getOrderId(), order.getTradedAmount(), order.getTurnover());
                    // 推送订单成交
                    messagingTemplate.convertAndSend("/topic/altxch-market/order-completed/" + symbol + "/" + order.getMemberId(), order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单取消成功
     *
     * @param records
     */
    @KafkaListener(topics = "exchange-order-cancel-success", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderCanceled(List<ConsumerRecord<String, String>> records) {
        try {
            for (int i = 0; i < records.size(); i++) {
                ConsumerRecord<String, String> record = records.get(i);
                //logger.info("取消订单消息topic={},value={},size={}", record.topic(), record.value(), records.size());
                ExchangeOrder order = JSON.parseObject(record.value(), ExchangeOrder.class);
                String symbol = order.getSymbol();
                // 调用服务处理
                exchangeOrderService.cancelOrder(order.getOrderId(), order.getTradedAmount(), order.getTurnover());
                // 推送实时成交
                messagingTemplate.convertAndSend("/topic/altxch-market/order-canceled/" + symbol + "/" + order.getMemberId(),
                        order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "exchange-trade", containerFactory = "kafkaListenerContainerFactory")
    public void handleTrade(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            executor.submit(new HandleTradeThread(record));
        }
    }

    public class HandleTradeThread implements Runnable {
        private ConsumerRecord<String, String> record;

        private HandleTradeThread(ConsumerRecord<String, String> record) {
            this.record = record;
        }

        @Override
        public void run() {
            try {
                List<ExchangeTrade> trades = JSON.parseArray(record.value(), ExchangeTrade.class);
                String symbol = trades.get(0).getSymbol();
                CoinProcessor coinProcessor = coinProcessorFactory.getProcessor(symbol);
                for (ExchangeTrade trade : trades) {
                    // 成交明细处理
                    exchangeOrderService.processExchangeTrade(trade, secondReferrerAward);
                    // 推送订单成交订阅
                    ExchangeOrder buyOrder = exchangeOrderService.findOne(trade.getBuyOrderId());
                    ExchangeOrder sellOrder = exchangeOrderService.findOne(trade.getSellOrderId());
                    messagingTemplate.convertAndSend(
                            "/topic/altxch-market/order-trade/" + symbol + "/" + buyOrder.getMemberId(), buyOrder);
                    messagingTemplate.convertAndSend(
                            "/topic/altxch-market/order-trade/" + symbol + "/" + sellOrder.getMemberId(), sellOrder);
                }
                System.out.println(coinProcessor);
                // 处理K线行情
                if (coinProcessor != null) {
                    coinProcessor.process(trades);
                }
                exchangePushService.addTrades(symbol, trades);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
