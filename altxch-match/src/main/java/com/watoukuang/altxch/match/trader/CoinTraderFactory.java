package com.watoukuang.altxch.match.trader;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class CoinTraderFactory {
    private ConcurrentHashMap<String, CoinTrader> traderMap;

    public CoinTraderFactory() {
        traderMap = new ConcurrentHashMap<>();
    }

    public void addTrader(String symbol, CoinTrader trader) {
        if (!traderMap.containsKey(symbol)) {
            traderMap.put(symbol, trader);
        }
    }

    // 重置，即使已经存在也会覆盖
    public void resetTrader(String symbol, CoinTrader trader) {
        traderMap.put(symbol, trader);
    }

    public boolean containsTrader(String symbol) {
        return traderMap.containsKey(symbol);
    }

    public CoinTrader getTrader(String symbol) {
        return traderMap.get(symbol);
    }


}


