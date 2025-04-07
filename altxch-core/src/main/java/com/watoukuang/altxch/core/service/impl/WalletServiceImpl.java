package com.watoukuang.altxch.core.service.impl;

import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.mapper.MemberWalletMapper;
import com.watoukuang.altxch.core.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final MemberWalletMapper memberWalletMapper;

    @Override
    public MemberWalletDO findByCoinUnitAndMemberId(String symbol, Long memberId) {
        return memberWalletMapper.selectMemberWallet(symbol, memberId);
    }
}
