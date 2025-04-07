package com.watoukuang.altxch.core.dao.dataobj;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.watoukuang.altxch.core.domain.ExchangeOrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeOrderDO implements Serializable {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 挂单类型
     */
    private String orderType;

    /**
     * 买入或卖出量(对于市价买入单表)
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 交易对符号
     */
    private String symbol;

    /**
     * 成交量
     */
    private BigDecimal tradedAmount = BigDecimal.ZERO;

    /**
     * 成交额(对市价买单有用)
     */
    private BigDecimal turnover = BigDecimal.ZERO;

    /**
     * 币单位
     */
    private String coinSymbol;

    /**
     * 结算单位
     */
    private String baseSymbol;

    /**
     * 订单方向
     */
    private String direction;

    /**
     * 挂单价格
     */
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 挂单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long time;

    /**
     * 交易完成时间
     */
    private Long completedTime;

    /**
     * 取消时间
     */
    private Long canceledTime;

    /**
     * 是否使用折扣(0 不使用 1使用)
     */
    private String useDiscount;

    /**
     * 订单状态
     */
    private String state;

    /**
     * 订单详情列表
     */
    private List<ExchangeOrderDetail> detail;
}
