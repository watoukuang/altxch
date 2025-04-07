package com.watoukuang.altxch.core.dao.dataobj;

import com.watoukuang.altxch.common.enums.YnEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberWalletDO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 币种名称
     */
    private String unit;

    /**
     * 可用余额
     */
    private BigDecimal balance;

    /**
     * 钱包是否锁定，0否，1是。锁定后
     */
    private Integer isLock = YnEnum.NO.getCode();
}
