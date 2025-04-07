package com.watoukuang.altxch.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.watoukuang.altxch.common.enums.YnEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "t_member_wallet")
public class MemberWallet implements Serializable {
    /**
     * 钱包ID
     */
    private Integer id;

    /**
     * 币种ID
     */
    private Integer coinId;

    /**
     * 可用余额
     */
    private BigDecimal balance;

    /**
     * 冻结余额
     */
    private BigDecimal frozenBalance;

    /**
     * 钱包是否锁定，0否，1是。锁定后
     */
    private Integer isLock = YnEnum.NO.getCode();
}
