package com.watoukuang.altxch.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.member.client.WalletClient;
import com.watoukuang.altxch.member.common.Constant;
import com.watoukuang.altxch.member.dao.entity.AddressExt;
import com.watoukuang.altxch.member.dao.entity.CoinProtocol;
import com.watoukuang.altxch.member.dao.mapper.AddressExtMapper;
import com.watoukuang.altxch.member.dao.mapper.CoinProtocolMapper;
import com.watoukuang.altxch.member.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    //    private final WalletClient walletClient;
    private final AddressExtMapper addressExtMapper;
    private final CoinProtocolMapper coinProtocolMapper;

    @Override
    public AddressExt create(Long memberId, Integer protocol) {
        AddressExt addressExt = getAddressExt(memberId, protocol);
        if (addressExt != null) {
            return addressExt;
        }
        CoinProtocol coinProtocol = getCoinProtocol(protocol);
        String protocolName = coinProtocol.getProtocolName();
        if (StringUtils.isBlank(protocolName)) {
            throw new RuntimeException("当前协议ID不存在对应的协议!");
        }
        String account = Constant.U + memberId;
//        R<String> rtp = walletClient.getAddress(protocolName, account);
//        if (rtp.getCode() != 200) {
//            throw new RuntimeException("服务运行时错误!");
//        }
//        String address = rtp.getData();
//        return addAddressEx(memberId, address, protocol);
        return null;
    }

    private AddressExt addAddressEx(Long memberId, String address, Integer protocol) {
        AddressExt addressExt = new AddressExt();
        addressExt.setMemberId(memberId);
        addressExt.setAddress(address);
        addressExt.setCoinProtocol(protocol);
        addressExtMapper.insert(addressExt);
        return addressExt;
    }

    private AddressExt getAddressExt(Long memberId, Integer protocol) {
        QueryWrapper<AddressExt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId).eq("coin_protocol", protocol);
        return addressExtMapper.selectOne(queryWrapper);
    }

    private CoinProtocol getCoinProtocol(Integer protocol) {
        QueryWrapper<CoinProtocol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("protocol", protocol);
        return coinProtocolMapper.selectOne(queryWrapper);
    }
}
