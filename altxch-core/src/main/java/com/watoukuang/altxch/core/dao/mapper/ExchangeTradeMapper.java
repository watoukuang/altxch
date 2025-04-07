package com.watoukuang.altxch.core.dao.mapper;

import com.watoukuang.altxch.common.mybatis.dao.BaseMapperPlus;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeTradeMapper extends BaseMapperPlus<ExchangeTrade> {
}
