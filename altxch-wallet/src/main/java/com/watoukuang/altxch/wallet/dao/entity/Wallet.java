package com.watoukuang.altxch.wallet.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_wallet")
public class Wallet {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 可用余额
     */
    private BigDecimal balance;
    /**
     * 冻结金额
     */
    private BigDecimal frozenBalance;
    /**
     * 是否锁定（0：未锁定，1：锁定）
     */
    private Integer isLock;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 待释放总量
     */
    private BigDecimal toReleased;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 币种ID
     */
    private Long coinId;
}