package com.watoukuang.altxch.match.trader;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.common.exception.ServiceException;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.MergeOrder;
import com.watoukuang.altxch.core.domain.TradePlate;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import com.watoukuang.altxch.core.enums.ExchangeOrderTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Slf4j
public class CoinTrader {
    /**
     * 交易对
     */
    private String symbol;

    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * 交易币种的精度
     */
    private int coinScale = 4;
    /**
     * 基币的精度
     */
    private int baseCoinScale = 4;

    private boolean tradingHalt = false;

    private boolean ready = false;

    private Integer publishType;

    /**
     * 清盘时间
     */
    private String clearTime;

    private SimpleDateFormat dateTimeFormat;

    private TreeMap<BigDecimal, MergeOrder> buyLimitPriceQueue;

    private TreeMap<BigDecimal, MergeOrder> sellLimitPriceQueue;

    private LinkedList<ExchangeOrder> buyMarketPriceQueue;

    private LinkedList<ExchangeOrder> sellMarketPriceQueue;

    private TradePlate buyTradePlate;

    private TradePlate sellTradePlate;
    private static final int MAX_BATCH_SIZE = 1000;

    public CoinTrader(String symbol) {
        this.symbol = symbol;
        initialize();
    }

    private void initialize() {
        this.buyLimitPriceQueue = new TreeMap<>(Comparator.reverseOrder());
        this.sellLimitPriceQueue = new TreeMap<>(Comparator.naturalOrder());
        this.buyMarketPriceQueue = new LinkedList<>();
        this.sellMarketPriceQueue = new LinkedList<>();
        this.sellTradePlate = new TradePlate(symbol, OrderDirectionEnum.SELL.getKey());
        this.buyTradePlate = new TradePlate(symbol, OrderDirectionEnum.BUY.getKey());
        this.dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void stopTrading() {

    }

    public void trade(List<ExchangeOrder> tradeOrders) {
        for (ExchangeOrder tradeOrder : tradeOrders) {
            trade(tradeOrder);
        }
    }

    public void trade(ExchangeOrder tradeOrder) {
        String direction = tradeOrder.getDirection();
        QueuePair queuePair = getQueuePair(direction);
        if (queuePair == null) {
            throw new ServiceException("无效的订单方向:" + direction);
        }
        TreeMap<BigDecimal, MergeOrder> limitPriceQueue = queuePair.limitPriceQueue;
        matchLimitPriceWithLPList(tradeOrder, limitPriceQueue);
        // 如果限价单未完全成交，尝试与市价单交易
        BigDecimal amount = tradeOrder.getAmount();
        BigDecimal tradedAmount = tradeOrder.getTradedAmount();
        if (amount.compareTo(tradedAmount) > 0) {
            LinkedList<ExchangeOrder> marketPriceQueue = queuePair.marketPriceQueue;
            matchLimitPriceWithMPList(tradeOrder, marketPriceQueue);
        }
    }

    /**
     * 处理限价单(与限价单队列匹配)
     */
    private void matchLimitPriceWithLPList(ExchangeOrder tradeOrder, TreeMap<BigDecimal, MergeOrder> limitPriceQueue) {
        List<ExchangeTrade> exchangeTrades = new ArrayList<>();
        List<ExchangeOrder> completedOrders = new ArrayList<>();
        Iterator<Map.Entry<BigDecimal, MergeOrder>> limitPriceIterator = limitPriceQueue.entrySet().iterator();
        boolean exitLoop = false;
        while (!exitLoop && limitPriceIterator.hasNext()) {
            Map.Entry<BigDecimal, MergeOrder> entry = limitPriceIterator.next();
            MergeOrder mergeOrder = entry.getValue();
            if (!checkPriceMatch(tradeOrder, mergeOrder)) {
                break;
            }
            Iterator<ExchangeOrder> mergeOrderIterator = mergeOrder.iterator();
            while (mergeOrderIterator.hasNext()) {
                ExchangeOrder matchOrder = mergeOrderIterator.next();
                ExchangeTrade exchangeTrade = processOrderMatch(tradeOrder, matchOrder);
                exchangeTrades.add(exchangeTrade);
                if (matchOrder.isCompleted()) {
                    // 删除已完成的订单
                    mergeOrderIterator.remove();
                    // 添加到已完成订单列表
                    completedOrders.add(matchOrder);
                }
                if (tradeOrder.isCompleted()) {
                    exitLoop = true;
                    completedOrders.add(tradeOrder);
                    break;
                }
            }
            if (mergeOrder.size() == 0) {
                limitPriceIterator.remove();
            }
        }
        // 批量处理交易
        handleExchangeTrade(exchangeTrades);
        // 处理已完成的订单
        if (!completedOrders.isEmpty()) {
            orderCompleted(completedOrders);
            String direction = tradeOrder.getDirection();
            TradePlate plate = direction.equals(OrderDirectionEnum.BUY.getKey()) ?
                    sellTradePlate : buyTradePlate;
            log.info("plate:{}", JSON.toJSONString(plate));
            sendTradePlateMessage(plate);
        }
    }

    private void handleExchangeTrade(List<ExchangeTrade> exchangeTrades) {
        if (exchangeTrades.isEmpty()) {
            return;
        }
        final int maxSize = 1000;
        int tradeCount = exchangeTrades.size();
        // 如果交易数量超过最大批处理大小，分批发送
        for (int index = 0; index < tradeCount; index += maxSize) {
            int endIndex = Math.min(index + maxSize, tradeCount);
            List<ExchangeTrade> batch = exchangeTrades.subList(index, endIndex);
            log.info("handle exchange trade message:{}", JSON.toJSONString(batch));
            kafkaTemplate.send("exchange-trade", JSON.toJSONString(batch));
        }
    }


    /**
     * 处理限价单(与市价单进行匹配)
     */
    private void matchLimitPriceWithMPList(ExchangeOrder tradeOrder, LinkedList<ExchangeOrder> marketPriceQueue) {
        //如果还没有交易完，订单压入列表中

        BigDecimal tradedAmount = tradeOrder.getTradedAmount();
        BigDecimal amount = tradeOrder.getAmount();
        if (tradedAmount.compareTo(amount) < 0) {
            addLimitPriceOrder(tradeOrder);
        }
        //每个订单的匹配批量推送
//        handleExchangeTrade(exchangeTrades);
//        orderCompleted(completedOrders);
    }

    /**
     * 订单完成，发送消息
     */
    public void orderCompleted(List<ExchangeOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return;
        }

        final int maxSize = 1000;
        int size = orders.size();

        // 处理订单列表分批发送
        for (int index = 0; index < size; index += maxSize) {
            int endIndex = Math.min(index + maxSize, size); // 计算结束索引
            List<ExchangeOrder> subOrders = orders.subList(index, endIndex);
            kafkaTemplate.send("exchange-order-completed", JSON.toJSONString(subOrders));
        }
    }

    /**
     * 发送盘口变化消息
     */
    public void sendTradePlateMessage(TradePlate plate) {
        //防止并发引起数组越界，造成盘口倒挂
        synchronized (plate.getItems()) {
            kafkaTemplate.send("exchange-trade-plate", JSON.toJSONString(plate));
        }
    }

    /**
     * 添加限价单队列
     */
    public void addLimitPriceOrder(ExchangeOrder tradeOrder) {
        String orderType = tradeOrder.getOrderType();
        if (!ExchangeOrderTypeEnum.LIMIT_PRICE.getKey().equals(orderType)) {
            return;
        }
        String direction = tradeOrder.getDirection();
        TreeMap<BigDecimal, MergeOrder> limitPriceQueue;
        TradePlate tradePlate;
        if (OrderDirectionEnum.BUY.getKey().equals(direction)) {
            limitPriceQueue = buyLimitPriceQueue;
            tradePlate = buyTradePlate;
        } else {
            limitPriceQueue = sellLimitPriceQueue;
            tradePlate = sellTradePlate;
        }
        // 将订单添加到交易盘
        tradePlate.add(tradeOrder);
        if (ready) {
            sendTradePlateMessage(tradePlate);
        }
        synchronized (limitPriceQueue) {
            limitPriceQueue.computeIfAbsent(tradeOrder.getPrice(), k -> new MergeOrder()).add(tradeOrder);
        }
    }


    /**
     * process order match
     */
    private ExchangeTrade processOrderMatch(ExchangeOrder tradeOrder, ExchangeOrder exchangeOrder) {
        ExchangeTrade exchangeTrade = new ExchangeTrade();
        String symbol = tradeOrder.getSymbol();
        exchangeTrade.setSymbol(symbol);
        BigDecimal price = tradeOrder.getPrice();
        exchangeTrade.setPrice(price);
        // 计算成交量
        BigDecimal tradedAmount = calculateTradedAmount(tradeOrder, exchangeOrder);
        exchangeTrade.setAmount(tradedAmount);
        // 确定成交价格
        BigDecimal dealPrice = getDealPrice(tradeOrder, exchangeOrder);
        // 计算成交额，保留足够精度
        BigDecimal turnover = tradedAmount.multiply(dealPrice);
        updateOrderStats(tradeOrder, exchangeOrder, tradedAmount, turnover);
        exchangeTrade.setBuyTurnover(turnover);
        exchangeTrade.setSellTurnover(turnover);
        exchangeTrade.setDirection(tradeOrder.getDirection());
        setOrderIds(exchangeTrade, tradeOrder, exchangeOrder);
        exchangeTrade.setTime(Calendar.getInstance().getTimeInMillis());
        // 移除成交的订单
        removeFromTradePlate(tradeOrder, exchangeOrder, tradedAmount);
        return exchangeTrade;
    }

    private BigDecimal getDealPrice(ExchangeOrder tradeOrder, ExchangeOrder exchangeOrder) {
        return tradeOrder.getOrderType().equals(ExchangeOrderTypeEnum.LIMIT_PRICE.getKey())
                ? exchangeOrder.getPrice()
                : tradeOrder.getPrice();
    }

    private void updateOrderStats(ExchangeOrder tradeOrder, ExchangeOrder exchangeOrder, BigDecimal tradedAmount, BigDecimal turnover) {
        exchangeOrder.setTradedAmount(exchangeOrder.getTradedAmount().add(tradedAmount));
        exchangeOrder.setTurnover(exchangeOrder.getTurnover().add(turnover));
        tradeOrder.setTradedAmount(tradeOrder.getTradedAmount().add(tradedAmount));
        tradeOrder.setTurnover(tradeOrder.getTurnover().add(turnover));
    }

    private void setOrderIds(ExchangeTrade exchangeTrade, ExchangeOrder tradeOrder, ExchangeOrder exchangeOrder) {
        if (tradeOrder.getDirection().equals(OrderDirectionEnum.BUY.getKey())) {
            exchangeTrade.setBuyOrderId(tradeOrder.getOrderId());
            exchangeTrade.setSellOrderId(exchangeOrder.getOrderId());
        } else {
            exchangeTrade.setBuyOrderId(exchangeOrder.getOrderId());
            exchangeTrade.setSellOrderId(tradeOrder.getOrderId());
        }
    }

    private void removeFromTradePlate(ExchangeOrder tradeOrder, ExchangeOrder exchangeOrder, BigDecimal tradedAmount) {
        if (tradeOrder.getOrderType().equals(ExchangeOrderTypeEnum.LIMIT_PRICE.getKey())) {
            if (exchangeOrder.getDirection().equals(OrderDirectionEnum.BUY.getKey())) {
                buyTradePlate.remove(exchangeOrder, tradedAmount);
            } else {
                sellTradePlate.remove(exchangeOrder, tradedAmount);
            }
        }
    }

    /**
     * 计算订单交易额
     */
    private BigDecimal calculateTradedAmount(ExchangeOrder tradeOrder, ExchangeOrder exchangeTrade) {
        BigDecimal needAmount = tradeOrder.getAmount().subtract(tradeOrder.getTradedAmount());
        BigDecimal availAmount = exchangeTrade.getAmount().subtract(exchangeTrade.getTradedAmount());
        return (availAmount.compareTo(needAmount) >= 0 ? needAmount : availAmount);
    }

    private boolean checkPriceMatch(ExchangeOrder tradingOrder, MergeOrder mergeOrder) {
        String direction = tradingOrder.getDirection();
        BigDecimal tradePrice = tradingOrder.getPrice();
        BigDecimal mergePrice = mergeOrder.getPrice();
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            return tradePrice.compareTo(mergePrice) >= 0;
        }
        if (direction.equals(OrderDirectionEnum.SELL.getKey())) {
            return tradePrice.compareTo(mergePrice) <= 0;
        }
        return false;
    }


    private QueuePair getQueuePair(String direction) {
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            return new QueuePair(sellLimitPriceQueue, sellMarketPriceQueue);
        } else if (direction.equals(OrderDirectionEnum.SELL.getKey())) {
            return new QueuePair(buyLimitPriceQueue, buyMarketPriceQueue);
        }
        return null;
    }

    public boolean getReady() {
        return ready;
    }


    private static class QueuePair {
        TreeMap<BigDecimal, MergeOrder> limitPriceQueue;
        LinkedList<ExchangeOrder> marketPriceQueue;

        QueuePair(TreeMap<BigDecimal, MergeOrder> limitPriceQueue, LinkedList<ExchangeOrder> marketPriceQueue) {
            this.limitPriceQueue = limitPriceQueue;
            this.marketPriceQueue = marketPriceQueue;
        }
    }

    public TradePlate getTradePlate(String direction) {
        return direction.equals(OrderDirectionEnum.BUY.getKey()) ? buyTradePlate : sellTradePlate;
    }

}
