package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;

public interface WalletService {
    MemberWalletDO findByCoinUnitAndMemberId(String symbol, Long memberId);
}
