package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {
    private final MongoTemplate mongoTemplate;

    // KLine 和 Trade 的前缀常量
    private static final String KLINE_PREFIX = "exchange_kline_";
    private static final String TRADE_PREFIX = "exchange_trade_";

    /**
     * 查找所有 KLine 数据，按时间降序排列，限制为1000条
     *
     * @param symbol 交易对的符号
     * @param period K线的时间周期
     * @return KLine 列表
     */
    public List<KLine> findAllKLine(String symbol, String period) {
        Query query = createQuery(null, null, Sort.Direction.DESC, 1000);
        return mongoTemplate.find(query, KLine.class, KLINE_PREFIX + symbol + "_" + period);
    }

    /**
     * 查找指定时间范围内的 KLine 数据，按时间升序排列
     *
     * @param symbol   交易对的符号
     * @param fromTime 开始时间
     * @param toTime   结束时间
     * @param period   K线的时间周期
     * @return KLine 列表
     */
    public List<KLine> findAllKLine(String symbol, long fromTime, long toTime, String period) {
        Query query = createQuery(fromTime, toTime, Sort.Direction.ASC, null);
        return mongoTemplate.find(query, KLine.class, KLINE_PREFIX + symbol + "_" + period);
    }

    /**
     * 查找指定时间范围内的第一笔交易
     *
     * @param symbol   交易对的符号
     * @param fromTime 开始时间
     * @param toTime   结束时间
     * @return 第一笔交易
     */
    public ExchangeTrade findFirstTrade(String symbol, long fromTime, long toTime) {
        Query query = createQuery(fromTime, toTime, Sort.Direction.ASC, null);
        return mongoTemplate.findOne(query, ExchangeTrade.class, TRADE_PREFIX + symbol);
    }

    /**
     * 查找指定时间范围内的最后一笔交易
     *
     * @param symbol   交易对的符号
     * @param fromTime 开始时间
     * @param toTime   结束时间
     * @return 最后一笔交易
     */
    public ExchangeTrade findLastTrade(String symbol, long fromTime, long toTime) {
        Query query = createQuery(fromTime, toTime, Sort.Direction.DESC, null);
        return mongoTemplate.findOne(query, ExchangeTrade.class, TRADE_PREFIX + symbol);
    }

    /**
     * 查找指定时间范围内的交易，按指定排序
     *
     * @param symbol   交易对的符号
     * @param fromTime 开始时间
     * @param toTime   结束时间
     * @param order    排序条件
     * @return 指定交易
     */
    public ExchangeTrade findTrade(String symbol, long fromTime, long toTime, Sort.Order order) {
        Query query = createQuery(fromTime, toTime, order.getDirection(), null);
        return mongoTemplate.findOne(query, ExchangeTrade.class, TRADE_PREFIX + symbol);
    }

    /**
     * 查找指定时间范围内的所有交易
     *
     * @param symbol    交易对的符号
     * @param timeStart 开始时间
     * @param timeEnd   结束时间
     * @return 交易列表
     */
    public List<ExchangeTrade> findTradeByTimeRange(String symbol, long timeStart, long timeEnd) {
        Query query = createQuery(timeStart, timeEnd, Sort.Direction.ASC, null);
        return mongoTemplate.find(query, ExchangeTrade.class, TRADE_PREFIX + symbol);
    }

    /**
     * 保存 KLine 数据
     *
     * @param symbol 交易对的符号
     * @param kLine  要保存的 KLine 对象
     */
    public void saveKLine(String symbol, KLine kLine) {
        mongoTemplate.insert(kLine, KLINE_PREFIX + symbol + "_" + kLine.getPeriod());
    }

    /**
     * 查找某时间段内的交易量
     *
     * @param symbol    交易对的符号
     * @param timeStart 开始时间
     * @param timeEnd   结束时间
     * @return 交易量总和
     */
    public BigDecimal findTradeVolume(String symbol, long timeStart, long timeEnd) {
        Query query = createQuery(timeStart, timeEnd, Sort.Direction.ASC, null);
        List<KLine> kLines = mongoTemplate.find(query, KLine.class, KLINE_PREFIX + symbol + "_1min");

        // 计算总交易量
        return kLines.stream()
                .map(KLine::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 创建查询条件的私有方法
     *
     * @param fromTime  开始时间
     * @param toTime    结束时间
     * @param direction 排序方向
     * @param limit     查询结果限制
     * @return 构建的查询对象
     */
    private Query createQuery(Long fromTime, Long toTime, Sort.Direction direction, Integer limit) {
        Criteria criteria = Criteria.where("time");

        // 如果有开始时间，则添加条件
        if (fromTime != null) {
            criteria = criteria.gte(fromTime);
        }
        // 如果有结束时间，则添加条件
        if (toTime != null) {
            criteria = criteria.lte(toTime);
        }

        // 创建查询对象
        Query query = new Query(criteria).with(Sort.by(direction, "time"));
        if (limit != null) {
            query.limit(limit);
        }

        return query;
    }
}