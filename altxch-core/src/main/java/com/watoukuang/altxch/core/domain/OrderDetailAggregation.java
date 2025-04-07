package com.watoukuang.altxch.core.domain;

import com.watoukuang.altxch.core.enums.ExchangeOrderTypeEnum;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "order_detail_aggregation")
public class OrderDetailAggregation {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 业务订单类型
     */
    private String bizOrderType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 会员真实姓名
     */
    private String realName;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 此聚合信息生成时间
     */
    private long time;

    /**
     * 手续费
     */
    private double fee;

    /**
     * 数量
     */
    private double amount;

    /**
     * 币种单位
     */
    private String unit;

    /**
     * exchange订单专有属性
     */
    private String direction;

    /**
     * 交易对象id
     * otc订单专有属性
     */
    private Long customerId;

    /**
     * 交易对象用户名
     * otc订单专有属性
     */
    private String customerName;


    private String customerRealName;

}
