package com.watoukuang.altxch.core.dao.mapper;

import com.watoukuang.altxch.common.mybatis.dao.BaseMapperPlus;
import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.entity.MemberWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface MemberWalletMapper extends BaseMapperPlus<MemberWallet> {
    MemberWalletDO selectMemberWallet(@Param("unit") String unit, @Param("memberId") Long memberId);

    /**
     * 添加钱包余额
     *
     * @param id
     * @param amount
     */
    void increaseBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);

    /**
     * 减少钱包余额
     *
     * @param id
     * @param amount
     */
    void decreaseFrozen(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
