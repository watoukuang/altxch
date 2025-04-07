package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.dto.KLineGraphDTO;

import java.util.List;

public interface ExchangeService {
    R<Object> getHistoryKLineGraph(KLineGraphDTO kLineGraphDTO);

}
