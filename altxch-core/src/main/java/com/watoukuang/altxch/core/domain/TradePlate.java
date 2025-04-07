package com.watoukuang.altxch.core.domain;

import com.alibaba.fastjson.JSONObject;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.enums.ExchangeOrderTypeEnum;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 盘口信息
 */
@Data
@Slf4j
public class TradePlate {
    /**
     * 存储交易项的列表
     */
    private List<TradePlateItem> items;
    /**
     * 最大深度限制
     */
    private int maxDepth = 100;

    /**
     * 交易方向(买入或者卖出)
     */
    private String direction;

    /**
     * 交易的符号
     */
    private String symbol;

    public TradePlate() {
    }

    /**
     * 带交易对和方向构造函数
     *
     * @param symbol    交易所
     * @param direction 方向
     */
    public TradePlate(String symbol, String direction) {
        this.direction = direction;
        this.symbol = symbol;
        items = Collections.synchronizedList(new LinkedList<TradePlateItem>());
    }


    /**
     * 将一个交易订单添加到交易盘中
     */
    public boolean add(ExchangeOrder exchangeOrder) {
        log.info("add trade plate order={}", exchangeOrder);
        synchronized (items) {
            int index = 0;
            // 获取订单类型和方向
            String orderType = exchangeOrder.getOrderType();
            String orderDirection = exchangeOrder.getDirection();
            if (orderType.equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey()) || !orderDirection.equals(direction)) {
                return false;
            }
            if (!items.isEmpty()) {
                for (index = 0; index < items.size(); index++) {
                    TradePlateItem item = items.get(index);
                    if (orderDirection.equals(OrderDirectionEnum.BUY.getKey()) && item.getPrice().compareTo(exchangeOrder.getPrice()) > 0
                            || orderDirection.equals(OrderDirectionEnum.SELL.getKey()) && item.getPrice().compareTo(exchangeOrder.getPrice()) < 0) {
                        continue;
                    } else if (item.getPrice().compareTo(exchangeOrder.getPrice()) == 0) {
                        // 剩余数量=订单的总数量-已成交的数量
                        BigDecimal deltaAmount = exchangeOrder.getAmount().subtract(exchangeOrder.getTradedAmount());
                        item.setAmount(item.getAmount().add(deltaAmount));
                        return true;
                    } else {
                        break;
                    }
                }
            }
            if (index < maxDepth) {
                TradePlateItem newItem = new TradePlateItem();
                newItem.setAmount(exchangeOrder.getAmount().subtract(exchangeOrder.getTradedAmount()));
                newItem.setPrice(exchangeOrder.getPrice());
                items.add(index, newItem);
            }
        }
        return true;
    }

    public void remove(ExchangeOrder order, BigDecimal amount) {
        synchronized (items) {
            //log.info("items>>init_size={},orderPrice={}",items.size(),order.getPrice());
            for (int index = 0; index < items.size(); index++) {
                TradePlateItem item = items.get(index);
                if (item.getPrice().compareTo(order.getPrice()) == 0) {
                    item.setAmount(item.getAmount().subtract(amount));
                    if (item.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        items.remove(index);
                    }
                    //log.info("items>>final_size={},itemAmount={},itemPrice={}",items.size(),item.getAmount(),item.getPrice());
                    return;
                }
            }
            log.info("items>>return_size={}", items.size());
        }
    }

    public void remove(ExchangeOrder order) {
        remove(order, order.getAmount().subtract(order.getTradedAmount()));
    }

    public void setItems(LinkedList<TradePlateItem> items) {
        this.items = items;
    }

    public BigDecimal getHighestPrice() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            return items.get(0).getPrice();
        } else {
            return items.get(items.size() - 1).getPrice();
        }
    }

    public int getDepth() {
        return items == null ? 0 : items.size();
    }


    public BigDecimal getLowestPrice() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            return items.get(items.size() - 1).getPrice();
        } else {
            return items.get(0).getPrice();
        }
    }

    /**
     * 获取委托量最大的档位
     */
    public BigDecimal getMaxAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimal.ZERO;
        for (TradePlateItem item : items) {
            if (item.getAmount().compareTo(amount) > 0) {
                amount = item.getAmount();
            }
        }
        return amount;

    }

    /**
     * 获取委托量最小的档位
     */
    public BigDecimal getMinAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = items.get(0).getAmount();
        for (TradePlateItem item : items) {
            if (item.getAmount().compareTo(amount) < 0) {
                amount = item.getAmount();
            }
        }
        return amount;
    }

    public JSONObject toJSON(int limit) {
        JSONObject json = new JSONObject();
        json.put("direction", direction);
        json.put("maxAmount", getMaxAmount());
        json.put("minAmount", getMinAmount());
        json.put("highestPrice", getHighestPrice());
        json.put("lowestPrice", getLowestPrice());
        json.put("symbol", getSymbol());
        json.put("items", items.size() > limit ? items.subList(0, limit) : items);
        return json;
    }


}
