package com.watoukuang.altxch.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.watoukuang.altxch.common.enums.YnEnum;
import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.common.mybatis.utils.PageUtils;
import com.watoukuang.altxch.core.dao.mapper.ExchangeCoinMapper;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.dto.ExchangeCoinDTO;
import com.watoukuang.altxch.core.domain.dto.PageExchangeCoinDTO;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeCoinServiceImpl extends BaseServiceImpl<ExchangeCoinMapper, ExchangeCoin> implements ExchangeCoinService {
    private final ExchangeCoinMapper exchangeCoinMapper;

    @Override
    public ExchangeCoin findBySymbol(String symbol) {
        LambdaQueryWrapper<ExchangeCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeCoin::getSymbol, symbol);
        return exchangeCoinMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ExchangeCoin> findAllEnabled() {
        LambdaQueryWrapper<ExchangeCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeCoin::getEnable, YnEnum.YES.getCode());
        return exchangeCoinMapper.selectList(queryWrapper);
    }

    @Override
    public void addExchangeCoin(ExchangeCoinDTO exchangeCoinDTO) {
        ExchangeCoin exchangeCoin = new ExchangeCoin();
        BeanUtils.copyProperties(exchangeCoinDTO, exchangeCoin);
        exchangeCoinMapper.insert(exchangeCoin);
    }

    @Override
    public Object pageExchangeCoins(PageExchangeCoinDTO pageDTO) {
        PageUtils.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<ExchangeCoin> mockapis = exchangeCoinMapper.selectExchangeCoins(pageDTO);
        return PageUtils.getPage(mockapis);
    }
}
