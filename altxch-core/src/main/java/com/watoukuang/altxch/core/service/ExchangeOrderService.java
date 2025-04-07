package com.watoukuang.altxch.core.service;

import com.github.pagehelper.PageInfo;
import com.watoukuang.altxch.common.mybatis.service.BaseService;
import com.watoukuang.altxch.core.dao.dataobj.ExchangeOrderDO;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.dto.PageOrderDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeOrderService extends BaseService<ExchangeOrder> {
    int findCurrentTradingCount(Long id, String symbol, Integer direction);

    void addOrder(Long id, ExchangeOrder order);

    List<ExchangeOrder> findAllTradingOrderBySymbol(String symbol);

    void tradeCompleted(String orderId, BigDecimal tradedAmount, BigDecimal turnover);

    void processExchangeTrade(ExchangeTrade trade, boolean secondReferrerAward) throws Exception;

    ExchangeOrder findOne(String buyOrderId);

    PageInfo<ExchangeOrderDO> pageCurrentOrder(PageOrderDTO pageCurrentOrderDTO);

    PageInfo<ExchangeOrderDO> pageHistoryOrder(PageOrderDTO pageOrderDTO);

    void cancelOrder(String orderId, BigDecimal tradedAmount, BigDecimal turnover);
}
