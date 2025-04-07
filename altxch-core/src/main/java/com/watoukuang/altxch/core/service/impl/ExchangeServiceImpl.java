package com.watoukuang.altxch.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.KLine;
import com.watoukuang.altxch.core.domain.dto.KLineGraphDTO;
import com.watoukuang.altxch.core.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final MongoTemplate mongoTemplate;

    public R<Object> getHistoryKLineGraph(KLineGraphDTO kLineGraphDTO) {
        String resolution = kLineGraphDTO.getResolution();
        String symbol = kLineGraphDTO.getSymbol();

        // 获取当前时间
        long toTime = System.currentTimeMillis();

        // 计算 30 分钟前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toTime);
        calendar.add(Calendar.MINUTE, -30); // 往前推 30 分钟
        long fromTime = calendar.getTimeInMillis();

        // 使用方法提取来处理时间间隔
        String period = determinePeriod(resolution);

        // 构建查询条件
        Query query = new Query()
                .with(Sort.by(Sort.Direction.ASC, "time"))
                .addCriteria(Criteria.where("time").gte(fromTime).lte(toTime));

        // 查询 K 线数据
        List<KLine> kLines = mongoTemplate.find(query, KLine.class, "exchange_kline_" + symbol + "_" + period);

        // 构建 JSON 数组
        JSONArray array = new JSONArray();
        for (KLine item : kLines) {
            JSONArray group = new JSONArray();
            group.add(0, item.getTime());
            group.add(1, item.getOpenPrice());
            group.add(2, item.getHighestPrice());
            group.add(3, item.getLowestPrice());
            group.add(4, item.getClosePrice());
            group.add(5, item.getVolume());
            array.add(group);
        }

        return R.ok(array);
    }

    /**
     * 根据分辨率确定 K 线的时间间隔
     *
     * @param resolution K 线分辨率
     * @return 时间间隔字符串
     */
    private String determinePeriod(String resolution) {
        if (resolution.endsWith("H") || resolution.endsWith("h")) {
            return resolution.substring(0, resolution.length() - 1) + "hour";
        } else if (resolution.endsWith("D") || resolution.endsWith("d")) {
            return resolution.substring(0, resolution.length() - 1) + "day";
        } else if (resolution.endsWith("W") || resolution.endsWith("w")) {
            return resolution.substring(0, resolution.length() - 1) + "week";
        } else if (resolution.endsWith("M") || resolution.endsWith("m")) {
            return resolution.substring(0, resolution.length() - 1) + "month";
        } else {
            Integer val = Integer.parseInt(resolution);
            return (val < 60) ? resolution + "min" : (val / 60) + "hour";
        }
    }

    private static List<KLine> generateMockData(int count) {
        List<KLine> kLines = new ArrayList<>();
        Random random = new Random();
        long currentTime = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            long time = currentTime + (i * 60000); // 每分钟一个数据
            BigDecimal openPrice = BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP); // 随机开盘价
            BigDecimal closePrice = openPrice.add(BigDecimal.valueOf(random.nextDouble() - 0.5).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10))); // 随机收盘价
            BigDecimal highestPrice = openPrice.max(closePrice).add(BigDecimal.valueOf(random.nextDouble() * 5).setScale(2, RoundingMode.HALF_UP)); // 随机最高价
            BigDecimal lowestPrice = openPrice.min(closePrice).subtract(BigDecimal.valueOf(random.nextDouble() * 5).setScale(2, RoundingMode.HALF_UP)); // 随机最低价
            BigDecimal volume = BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP); // 随机交易量

            kLines.add(new KLine(time, openPrice, highestPrice, lowestPrice, closePrice, volume));
        }

        return kLines;
    }
}
