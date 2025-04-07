package com.watoukuang.altxch.core.domain;

import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MergeOrder {
    /**
     * 存储所有的交易订单列表
     */
    private final List<ExchangeOrder> orders = new ArrayList<>();


    /**
     * 在订单列表的末尾添加一个新的交易订单
     */
    public void add(ExchangeOrder order) {
        orders.add(order);
    }

    /**
     * 获取订单列表中的第一个交易订单
     */
    public ExchangeOrder get() {
        return orders.get(0);
    }

    /**
     * 返回订单列表的大小(订单数量)
     */
    public int size() {
        return orders.size();
    }

    /**
     * 获取第一个交易订单的价格
     */
    public BigDecimal getPrice() {
        return orders.get(0).getPrice();
    }

    /**
     * 获取订单列表的迭代器，以便可以遍历订单
     */
    public Iterator<ExchangeOrder> iterator() {
        return orders.iterator();
    }

    /**
     * 计算所有交易订单的总金额
     */
    public BigDecimal getTotalAmount() {
        // 初始化总金额为0
        BigDecimal total = new BigDecimal(0);
        // 遍历所有订单，累加每个订单的金额
        for (ExchangeOrder item : orders) {
            total = total.add(item.getAmount());
        }
        return total;
    }
}
