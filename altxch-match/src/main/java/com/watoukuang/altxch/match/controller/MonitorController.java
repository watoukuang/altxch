package com.watoukuang.altxch.match.controller;

import com.alibaba.fastjson.JSONObject;
import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.TradePlateItem;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import com.watoukuang.altxch.match.trader.CoinTrader;
import com.watoukuang.altxch.match.trader.CoinTraderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final CoinTraderFactory factory;

    @GetMapping(value = "getTradePlate")
    public R<Object> getTraderPlate() {
        Map<String, List<TradePlateItem>> result = new HashMap<>();
        String symbol = "BTC/USDT";
        CoinTrader trader = factory.getTrader(symbol);
        if (trader == null) {
            return null;
        }
        result.put("bid", trader.getTradePlate(OrderDirectionEnum.BUY.getKey()).getItems());
        result.put("ask", trader.getTradePlate(OrderDirectionEnum.SELL.getKey()).getItems());
        return R.ok(result);
    }

    @GetMapping(value = "getTradePlateMini")
    public R<Map<String, JSONObject>> getTraderPlateMini() {
        Map<String, JSONObject> result = new HashMap<>();
        String symbol = "BTC/USDT";
        CoinTrader trader = factory.getTrader(symbol);
        if (trader == null) {
            return null;
        }
        result.put("bid", trader.getTradePlate(OrderDirectionEnum.BUY.getKey()).toJSON(24));
        result.put("ask", trader.getTradePlate(OrderDirectionEnum.SELL.getKey()).toJSON(24));
        return R.ok(result);
    }
}
