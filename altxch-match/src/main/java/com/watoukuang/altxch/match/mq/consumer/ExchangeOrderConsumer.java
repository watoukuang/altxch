package com.watoukuang.altxch.match.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.match.trader.CoinTrader;
import com.watoukuang.altxch.match.trader.CoinTraderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeOrderConsumer {

    private final CoinTraderFactory traderFactory;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "exchange-order", groupId = "kafkaListenerContainerFactory")
    public void onOrderSubmitted(List<ConsumerRecord<String, String>> records) {
        records.forEach(record -> {
            log.info("接收订单>>topic={}, value={}, size={}", record.topic(), record.value(), records.size());
            ExchangeOrder order = JSON.parseObject(record.value(), ExchangeOrder.class);
            // 订单解析失败，直接返回
            if (order == null) {
                return;
            }
            CoinTrader trader = traderFactory.getTrader(order.getSymbol());
            // 检查交易状态
            if (trader.isTradingHalt() || !trader.getReady()) {
                // 撤回当前等待的订单
                kafkaTemplate.send("exchange-order-cancel-success", JSON.toJSONString(order));
                return;
            }
            processTrade(order, trader);
        });
    }

    private void processTrade(ExchangeOrder order, CoinTrader trader) {
        try {
            long startTick = System.currentTimeMillis();
            trader.trade(order);
            log.info("complete trade, {}ms used!", System.currentTimeMillis() - startTick);
        } catch (Exception e) {
            log.error("====交易出错，退回订单 ====", e);
            // 发送取消订单消息
            kafkaTemplate.send("exchange-order-cancel-success", JSON.toJSONString(order));
        }
    }

    @KafkaListener(topics = "exchange-order-cancel", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCancel(List<ConsumerRecord<String, String>> records) {
        records.forEach(record -> {
            log.info("取消订单 topic={}, key={}, size={}", record.topic(), record.key(), records.size());
            ExchangeOrder order = JSON.parseObject(record.value(), ExchangeOrder.class);
            // 订单解析失败，直接返回
            if (order == null) {
                return;
            }
            CoinTrader trader = traderFactory.getTrader(order.getSymbol());
            if (trader.getReady()) {
                cancelOrder(order, trader);
            }
        });
    }

    private void cancelOrder(ExchangeOrder order, CoinTrader trader) {
        try {
//            ExchangeOrder result = trader.cancelOrder(order);
//            if (result != null) {
//                // 发送取消成功消息
//                kafkaTemplate.send("exchange-order-cancel-success", JSON.toJSONString(result));
//            }
        } catch (Exception e) {
            log.error("====取消订单出错 ====", e);
        }
    }
}
