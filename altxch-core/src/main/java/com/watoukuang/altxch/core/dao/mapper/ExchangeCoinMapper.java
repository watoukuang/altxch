package com.watoukuang.altxch.core.dao.mapper;

import com.watoukuang.altxch.common.mybatis.dao.BaseMapperPlus;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.dto.PageExchangeCoinDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeCoinMapper extends BaseMapperPlus<ExchangeCoin> {
    List<ExchangeCoin> selectExchangeCoins(@Param("pageDTO") PageExchangeCoinDTO pageDTO);
}
