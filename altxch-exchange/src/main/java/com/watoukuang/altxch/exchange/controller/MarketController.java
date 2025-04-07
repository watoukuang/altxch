package com.watoukuang.altxch.exchange.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.domain.CoinExchangeRate;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.processor.CoinProcessor;
import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "market")
@RequiredArgsConstructor
public class MarketController {
    private final ExchangeCoinService exchangeCoinService;
    private final CoinProcessorFactory coinProcessorFactory;

    @RequestMapping(value = "symbol-thumb-trend")
    public R<JSONArray> findSymbolThumbWithTrend() {
        // 获取所有可见的交易币种
        List<ExchangeCoin> coins = exchangeCoinService.findAllEnabled();
        // 创建一个日历实例并将时间字段设置为整点
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        long nowTime = calendar.getTimeInMillis();
        // 获取24小时前的时间戳
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        long firstTimeOfToday = calendar.getTimeInMillis();
        JSONArray array = new JSONArray();
        for (ExchangeCoin coin : coins) {
            // 获取对应币种的处理器
            CoinProcessor processor = coinProcessorFactory.getProcessor(coin.getSymbol());
            // 获取币种的概要信息
            CoinThumb thumb = processor.getThumb();
            JSONObject json = (JSONObject) JSON.toJSON(thumb);
            json.put("zone", coin.getZone());

            // 将当前币种的 JSON 对象添加到结果数组中
            array.add(json);
        }
        return R.ok(array);
    }
}
