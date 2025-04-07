package com.watoukuang.altxch.match.event;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.TradePlate;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import com.watoukuang.altxch.core.service.ExchangeOrderDetailService;
import com.watoukuang.altxch.core.service.ExchangeOrderService;
import com.watoukuang.altxch.match.trader.CoinTrader;
import com.watoukuang.altxch.match.trader.CoinTraderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoinTraderEvent implements ApplicationListener<ContextRefreshedEvent> {
    private final CoinTraderFactory coinTraderFactory;
    private final ExchangeOrderService exchangeOrderService;
    private final ExchangeOrderDetailService exchangeOrderDetailService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("CoinTrader engine init...");
        Map<String, CoinTrader> traders = coinTraderFactory.getTraderMap();
        if (traders == null || traders.isEmpty()) {
            return;
        }
        traders.forEach(this::processTrader);
        log.info("CoinTrader engine initialized successfully...");
    }

    private void processTrader(String symbol, CoinTrader trader) {
        log.info("======CoinTrader Process: {} ======", symbol);
        List<ExchangeOrder> orders = exchangeOrderService.findAllTradingOrderBySymbol(symbol);
        log.info("Initialize: found {} trading orders", orders.size());
        List<ExchangeOrder> tradingOrders = new ArrayList<>();
        List<ExchangeOrder> completedOrders = new ArrayList<>();
        // TODO 处理每个订单(保留，目前我也不知道具体的逻辑)
        orders.forEach(order -> processOrder(order, tradingOrders, completedOrders));
        log.info("Initialize: tradingOrders total count( {})", tradingOrders.size());
        trader.trade(tradingOrders);
        // check completed orders and send notification message...
        if (!completedOrders.isEmpty()) {
            log.info("Initialize: completedOrders total count( {})", completedOrders.size());
            // send message for completed orders
            kafkaTemplate.send("exchange-order-completed", JSON.toJSONString(completedOrders));
        }
        trader.setReady(true);
    }

    /**
     * process each order
     *
     * @param order           current order
     * @param tradingOrders   List to store incomplete orders
     * @param completedOrders List to store completed orders
     */
    private void processOrder(ExchangeOrder order, List<ExchangeOrder> tradingOrders, List<ExchangeOrder> completedOrders) {
        BigDecimal tradedAmount = BigDecimal.ZERO;
        BigDecimal turnover = BigDecimal.ZERO;
//        List<ExchangeOrderDetail> details = exchangeOrderDetailService.findAllByOrderId(order.getOrderId());
//        for (ExchangeOrderDetail trade : details) {
//            tradedAmount = tradedAmount.add(trade.getAmount()); // 累加已交易金额
//            turnover = turnover.add(trade.getAmount().multiply(trade.getPrice())); // 累加成交额
//        }
        // update order information
        order.setTradedAmount(tradedAmount);
        order.setTurnover(turnover);

        // Classify based on order status
        if (!order.isCompleted()) {
            tradingOrders.add(order);
        } else {
            completedOrders.add(order);
        }
    }

}
