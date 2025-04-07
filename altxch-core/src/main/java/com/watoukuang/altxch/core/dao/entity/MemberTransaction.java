package com.watoukuang.altxch.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName(value = "t_member_transaction")
public class MemberTransaction {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 交易类型
     */
    private String type;

    /**
     * 币种名称，如 BTC
     */
    private String symbol;

    /**
     * 充值或提现地址、或转账地址
     */
    private String address;

    /**
     * 交易手续费
     * 提现和转账才有手续费，充值没有;如果是法币交易，只收发布广告的那一方的手续费
     */
    private BigDecimal fee = BigDecimal.ZERO;

    /**
     * 标识位，特殊情况会用到，默认为0
     */
    private int flag = 0;
    /**
     * 实收手续费
     */
    private BigDecimal realFee;

    /**
     * 折扣手续费
     */
    private BigDecimal discountFee;

    /**
     * 是否已返佣，0：否，1：是
     */
    private int isReward;
}
