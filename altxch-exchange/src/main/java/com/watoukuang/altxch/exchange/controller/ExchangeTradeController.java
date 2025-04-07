package com.watoukuang.altxch.exchange.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.dto.LatestTradeDTO;
import com.watoukuang.altxch.core.service.ExchangeTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "exchangeTrade")
@RequiredArgsConstructor
public class ExchangeTradeController {

    private final ExchangeTradeService exchangeTradeService;

    @PostMapping(value = "latest-trade")
    public R<List<ExchangeTrade>> listLatestTrades(@RequestBody LatestTradeDTO latestTradeDTO) {
        Integer size = latestTradeDTO.getSize();
        String symbol = latestTradeDTO.getSymbol();
        return R.ok(exchangeTradeService.listLatestTrades(symbol, size));
    }
}
