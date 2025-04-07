package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.common.mybatis.service.BaseService;
import com.watoukuang.altxch.core.dao.entity.Coin;

public interface CoinService extends BaseService<Coin> {
    Coin findByUnit(String exCoin);
}
