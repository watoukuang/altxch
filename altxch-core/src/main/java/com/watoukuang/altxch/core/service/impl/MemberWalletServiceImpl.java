package com.watoukuang.altxch.core.service.impl;

import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.core.dao.mapper.MemberWalletMapper;
import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.entity.MemberWallet;
import com.watoukuang.altxch.core.service.MemberWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWalletServiceImpl extends BaseServiceImpl<MemberWalletMapper, MemberWallet> implements MemberWalletService {
    private final MemberWalletMapper memberWalletMapper;

    @Override
    public MemberWalletDO findByCoinUnitAndMemberId(String unit, Long memberId) {
        return memberWalletMapper.selectMemberWallet(unit, memberId);
    }
}
