package com.watoukuang.altxch.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.core.dao.mapper.CoinMapper;
import com.watoukuang.altxch.core.dao.entity.Coin;
import com.watoukuang.altxch.core.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl extends BaseServiceImpl<CoinMapper, Coin> implements CoinService {
    private final CoinMapper coinMapper;

    @Override
    public Coin findByUnit(String exCoin) {
        LambdaQueryWrapper<Coin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coin::getUnit, exCoin);
        return coinMapper.selectOne(queryWrapper);
    }
}
