package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.common.mybatis.service.BaseService;
import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.entity.MemberWallet;

public interface MemberWalletService extends BaseService<MemberWallet> {
    MemberWalletDO findByCoinUnitAndMemberId(String unit, Long memberId);
}
