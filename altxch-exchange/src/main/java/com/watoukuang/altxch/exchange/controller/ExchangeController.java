package com.watoukuang.altxch.exchange.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.dto.KLineGraphDTO;
import com.watoukuang.altxch.core.processor.CoinProcessor;
import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import com.watoukuang.altxch.core.service.ExchangeService;
import com.watoukuang.altxch.exchange.client.AltxchMatchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final AltxchMatchClient matchClient;
    private final ExchangeService exchangeService;
    private final ExchangeCoinService exchangeCoinService;
    private final CoinProcessorFactory coinProcessorFactory;

    @GetMapping(value = "exchange-plate")
    public R<Object> findTradePlate(String symbol) {
        return matchClient.getTraderPlate();
    }

    @GetMapping(value = "exchange-plate-mini")
    public R<Object> findTradePlateMini(String symbol) {
        return matchClient.getTradePlateMini();
    }

    @PostMapping(value = "history")
    public R<Object> getHistoryKLineGraph(@RequestBody KLineGraphDTO kLineGraphDTO) {
        return exchangeService.getHistoryKLineGraph(kLineGraphDTO);
    }

    @PostMapping(value = "symbol-thumb")
    public R<List<CoinThumb>> listCoinThumb() {
        List<ExchangeCoin> exchangeCoins = exchangeCoinService.findAllEnabled();
        List<CoinThumb> thumbs = new ArrayList<>();
        for (ExchangeCoin exchangeCoin : exchangeCoins) {
            CoinProcessor processor = coinProcessorFactory.getProcessor(exchangeCoin.getSymbol());
            CoinThumb thumb = processor.getThumb();
            thumb.setZone(exchangeCoin.getZone());
            thumbs.add(thumb);
        }
        return R.ok(thumbs);
    }

    @GetMapping(value = "symbol-info")
    public R<ExchangeCoin> getSymbolInfo(@RequestParam String symbol) {
        ExchangeCoin exchangeCoin = exchangeCoinService.findBySymbol(symbol);
        return R.ok(exchangeCoin);
    }

}
