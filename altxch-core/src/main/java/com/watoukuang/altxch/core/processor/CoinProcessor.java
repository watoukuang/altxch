package com.watoukuang.altxch.core.processor;

import com.watoukuang.altxch.core.domain.CoinExchangeRate;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.handler.MarketHandler;
import com.watoukuang.altxch.core.service.MarketService;

import java.util.List;

public interface CoinProcessor {
    /**
     * 状态管理：用于控制处理状态，可能用于暂停或停止某些操作（如数据处理或 K 线生成）
     */
    void setIsHalt(boolean status);

    /**
     * 添加市场处理器，用于处理市场数据
     */
    void addHandler(MarketHandler mongoMarketHandler);

    /**
     * 设置市场服务，用于与市场数据交互
     */
    void setMarketService(MarketService marketService);

    /**
     * 设置是否停止 K 线生成
     */
    void setIsStopKLine(boolean b);

    /**
     * 设置币种汇率
     */
    void setCoinExchangeRate(CoinExchangeRate coinExchangeRate);

    /**
     * 处理交易数据
     */
    void process(List<ExchangeTrade> trades);

    /**
     * 初始化交易概况数据
     */
    void initializeThumb();

    /**
     * 初始化美元汇率
     */
    void initializeUsdRate();

    /**
     * 检查是否停止 K 线生成
     */
    boolean isStopKline();

    /**
     * 生成 K 线数据
     */
    void generateKLine(long time, int minute, int hour);

    /**
     * 获取币种缩略图
     */
    CoinThumb getThumb();
}
