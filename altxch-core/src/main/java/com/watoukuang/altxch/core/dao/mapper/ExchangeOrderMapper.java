package com.watoukuang.altxch.core.dao.mapper;

import com.watoukuang.altxch.common.mybatis.dao.BaseMapperPlus;
import com.watoukuang.altxch.core.dao.dataobj.ExchangeOrderDO;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.domain.dto.PageOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeOrderMapper extends BaseMapperPlus<ExchangeOrder> {
    List<ExchangeOrderDO> selectExchangeOrders(@Param("pageDTO") PageOrderDTO pageDTO);
}
