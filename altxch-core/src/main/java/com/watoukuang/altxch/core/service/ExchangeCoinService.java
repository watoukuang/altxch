package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.common.mybatis.service.BaseService;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.dto.ExchangeCoinDTO;
import com.watoukuang.altxch.core.domain.dto.PageExchangeCoinDTO;

import java.util.List;

public interface ExchangeCoinService extends BaseService<ExchangeCoin> {
    ExchangeCoin findBySymbol(String symbol);

    List<ExchangeCoin> findAllEnabled();

    void addExchangeCoin(ExchangeCoinDTO exchangeCoinDTO);

    Object pageExchangeCoins(PageExchangeCoinDTO pageExchangeCoinDTO);

}
