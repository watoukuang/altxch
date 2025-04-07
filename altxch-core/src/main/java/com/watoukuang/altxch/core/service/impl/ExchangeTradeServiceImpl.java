package com.watoukuang.altxch.core.service.impl;

import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.core.dao.mapper.ExchangeTradeMapper;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.service.ExchangeTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeTradeServiceImpl extends BaseServiceImpl<ExchangeTradeMapper, ExchangeTrade> implements ExchangeTradeService {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<ExchangeTrade> listLatestTrades(String symbol, int size) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "time"));
        Pageable page = PageRequest.of(0, size);
        query.with(page);
        query.addCriteria(Criteria.where("symbol").is(symbol));
        return mongoTemplate.find(query, ExchangeTrade.class, "exchange_trade_" + symbol);
    }
}
