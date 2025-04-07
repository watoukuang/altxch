package com.watoukuang.altxch.core.processor;

import com.alibaba.fastjson.JSON;
import com.watoukuang.altxch.core.domain.CoinExchangeRate;
import com.watoukuang.altxch.core.domain.CoinThumb;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.KLine;
import com.watoukuang.altxch.core.handler.MarketHandler;
import com.watoukuang.altxch.core.service.MarketService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 默认交易处理器，产生1mK线信息
 */
@Slf4j
@ToString
public class DefaultCoinProcessor implements CoinProcessor {
    /**
     * 货币符号（例如，“BTC”，“ETH”）
     */
    private String symbol;

    /**
     * 用于转换的基础货币（例如，“USD”，“EUR”）
     */
    private String baseCoin;

    /**
     * 当前K线（蜡烛图）数据
     */
    private KLine currentKLine;

    /**
     * 处理市场事件的市场处理器列表
     */
    private List<MarketHandler> handlers;

    /**
     * 货币的缩略信息（例如，logo，名称）
     */
    private CoinThumb coinThumb;

    /**
     * 处理市场相关操作的服务
     */
    private MarketService marketService;

    /**
     * 货币的汇率信息
     */
    private CoinExchangeRate coinExchangeRate;

    /**
     * 指示处理是否暂时停止的标志
     */
    private Boolean isHalt = true;

    /**
     * 指示K线生成是否停止的标志
     */
    private Boolean stopKLine = false;

    public DefaultCoinProcessor(String symbol, String baseCoin) {
        handlers = new ArrayList<>();
        this.baseCoin = baseCoin;
        this.symbol = symbol;
        createNewKLine();
    }

    private void createNewKLine() {
        currentKLine = new KLine();
        synchronized (currentKLine) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            //1Min时间要是下一整分钟的
            calendar.add(Calendar.MINUTE, 1);
            currentKLine.setTime(calendar.getTimeInMillis());
            currentKLine.setPeriod("1min");
            currentKLine.setCount(0);
        }
    }


    @Override
    public void addHandler(MarketHandler storage) {
        handlers.add(storage);
    }

    @Override
    public void setIsStopKLine(boolean b) {

    }

    @Override
    public void setCoinExchangeRate(CoinExchangeRate coinExchangeRate) {

    }

    @Override
    public void setIsHalt(boolean status) {
        this.isHalt = status;
    }

    @Override
    public void process(List<ExchangeTrade> trades) {
        if (isHalt) {
            return;
        }
        if (trades == null || trades.isEmpty()) {
            return;
        }
        synchronized (currentKLine) {
            for (ExchangeTrade exchangeTrade : trades) {
                //处理K线
                processTrade(currentKLine, exchangeTrade);
                //处理今日概况信息
                log.debug("处理今日概况信息");
                handleThumb(exchangeTrade);
                //存储并推送成交信息
                handleTradeStorage(exchangeTrade);
            }
        }
    }

    @Override
    public void initializeThumb() {
        // 获取当前时间的日历实例
        Calendar calendar = Calendar.getInstance();

        // 将秒和毫秒字段置为0，确保时间精确到分钟
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long nowTime = calendar.getTimeInMillis();

        // 将小时和分钟字段置为0，获取今天的开始时间（00:00:00）
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long firstTimeOfToday = calendar.getTimeInMillis();

        // 设置时间周期为1分钟
        String period = "1min";

        // 日志记录初始化时间范围
        log.info("initializeThumb from {} to {}", firstTimeOfToday, nowTime);

        // 从服务中获取指定时间范围的 K 线数据
        List<KLine> lines = marketService.findAllKLine(this.symbol, firstTimeOfToday, nowTime, period);

        // 初始化 CoinThumb 实例
        coinThumb = new CoinThumb();

        // 使用同步块确保线程安全
        synchronized (coinThumb) {
            coinThumb.setSymbol(symbol);

            for (KLine kline : lines) {
                // 跳过开盘价为零的 K 线
                if (kline.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                // 设置开盘价（首次）
                if (coinThumb.getOpen().compareTo(BigDecimal.ZERO) == 0) {
                    coinThumb.setOpen(kline.getOpenPrice());
                }

                // 更新最高价
                if (coinThumb.getHigh().compareTo(kline.getHighestPrice()) < 0) {
                    coinThumb.setHigh(kline.getHighestPrice());
                }

                // 更新最低价（需确保最低价大于零）
                if (kline.getLowestPrice().compareTo(BigDecimal.ZERO) > 0
                        && coinThumb.getLow().compareTo(kline.getLowestPrice()) > 0) {
                    coinThumb.setLow(kline.getLowestPrice());
                }

                // 更新收盘价（需确保收盘价大于零）
                if (kline.getClosePrice().compareTo(BigDecimal.ZERO) > 0) {
                    coinThumb.setClose(kline.getClosePrice());
                }

                // 累加交易量和成交额
                coinThumb.setVolume(coinThumb.getVolume().add(kline.getVolume()));
                coinThumb.setTurnover(coinThumb.getTurnover().add(kline.getTurnover()));
            }

            // 计算涨跌额和涨幅
            coinThumb.setChange(coinThumb.getClose().subtract(coinThumb.getOpen()));

            // 计算涨幅，使用开盘价作为标准
            if (coinThumb.getOpen().compareTo(BigDecimal.ZERO) > 0) {
                coinThumb.setChg(coinThumb.getChange().divide(coinThumb.getOpen(), 4, RoundingMode.UP));
            }
        }
    }

    @Override
    public void initializeUsdRate() {

    }

    @Override
    public boolean isStopKline() {
        return this.stopKLine;
    }


    @Override
    public void generateKLine(long time, int minute, int hour) {
        // 生成1分钟K线
        this.generateKLineByRange(1, Calendar.MINUTE, time);
    }

    @Override
    public CoinThumb getThumb() {
        return coinThumb;
    }

    public void generateKLineByRange(int range, int field, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        // 格式化时间的方法
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long endTick = calendar.getTimeInMillis();
        String endTime = df.format(calendar.getTime());

        // 往前推 range 个时间单位
        calendar.add(field, -range);
        long startTick = calendar.getTimeInMillis();
        String fromTime = df.format(calendar.getTime());

        log.info("Time range from {} to {}", fromTime, endTime);

        KLine kLine = new KLine();
        kLine.setTime(endTick);
        String rangeUnit = getRangeUnit(field);
        kLine.setPeriod(range + rangeUnit);

        List<ExchangeTrade> exchangeTrades = null;

        // 查询订单成交详情
        if (isShortTermKLine(field)) {
            exchangeTrades = marketService.findTradeByTimeRange(this.symbol, startTick, endTick);
            processTrades(kLine, exchangeTrades);
        } else { // 处理周线和月线
            processKline(kLine, startTick, endTick, field);
        }

        // 设置开盘价、收盘价、最低价、最高价
        setKLinePrices(kLine);

        log.info("{} Kline generated: {} in {}, data={}", this.symbol, range + rangeUnit, df.format(new Date(kLine.getTime())), JSON.toJSONString(kLine));
        handleKLineStorage(kLine);
    }

    /**
     * 根据字段返回时间范围单位
     */
    private String getRangeUnit(int field) {
        switch (field) {
            case Calendar.MINUTE:
                return "min";
            case Calendar.HOUR_OF_DAY:
                return "hour";
            case Calendar.DAY_OF_WEEK:
                return "week";
            case Calendar.DAY_OF_YEAR:
                return "day";
            case Calendar.DAY_OF_MONTH:
                return "month";
            default:
                return "unknown";
        }
    }

    /**
     * 判断是否为短期 K 线（分钟、小时、日）
     */
    private boolean isShortTermKLine(int field) {
        return field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY || field == Calendar.DAY_OF_YEAR;
    }

    /**
     * 处理交易记录，更新 K 线信息
     */
    private void processTrades(KLine kLine, List<ExchangeTrade> exchangeTrades) {
        for (ExchangeTrade exchangeTrade : exchangeTrades) {
            processTrade(kLine, exchangeTrade);
        }
    }

    /**
     * 设置 K 线的开盘价、收盘价、最低价、最高价
     */
    private void setKLinePrices(KLine kLine) {
        if (kLine.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
            kLine.setOpenPrice(coinThumb.getClose());
            kLine.setClosePrice(coinThumb.getClose());
            kLine.setLowestPrice(coinThumb.getClose());
            kLine.setHighestPrice(coinThumb.getClose());
        }
    }

    public void handleKLineStorage(KLine kLine) {
        for (MarketHandler storage : handlers) {
            storage.handleKLine(symbol, kLine);
        }
    }

    private void handleTradeStorage(ExchangeTrade exchangeTrade) {
        for (MarketHandler storage : handlers) {
            storage.handleTrade(symbol, exchangeTrade, coinThumb);
        }
    }

    private void handleThumb(ExchangeTrade exchangeTrade) {
        log.info("handleThumb symbol = {}", this.symbol);
        synchronized (coinThumb) {
            if (coinThumb.getOpen().compareTo(BigDecimal.ZERO) == 0) {
                //第一笔交易记为开盘价
                coinThumb.setOpen(exchangeTrade.getPrice());
            }
            coinThumb.setHigh(exchangeTrade.getPrice().max(coinThumb.getHigh()));
            if (coinThumb.getLow().compareTo(BigDecimal.ZERO) == 0) {
                coinThumb.setLow(exchangeTrade.getPrice());
            } else {
                coinThumb.setLow(exchangeTrade.getPrice().min(coinThumb.getLow()));
            }
            coinThumb.setClose(exchangeTrade.getPrice());
            coinThumb.setVolume(coinThumb.getVolume().add(exchangeTrade.getAmount()).setScale(4, RoundingMode.UP));
            BigDecimal turnover = exchangeTrade.getPrice().multiply(exchangeTrade.getAmount()).setScale(4, RoundingMode.UP);
            coinThumb.setTurnover(coinThumb.getTurnover().add(turnover));
            BigDecimal change = coinThumb.getClose().subtract(coinThumb.getOpen());
            coinThumb.setChange(change);
            if (coinThumb.getOpen().compareTo(BigDecimal.ZERO) > 0) {
                coinThumb.setChg(change.divide(coinThumb.getOpen(), 4, BigDecimal.ROUND_UP));
            }
            if ("USDT".equalsIgnoreCase(baseCoin)) {
                log.info("setUsdRate", exchangeTrade.getPrice());
                coinThumb.setUsdRate(exchangeTrade.getPrice());
            } else {

            }
            log.info("thumb = {}", coinThumb);
        }
    }

    private void processTrade(KLine kLine, ExchangeTrade exchangeTrade) {
        if (kLine.getClosePrice().compareTo(BigDecimal.ZERO) == 0) {
            //第一次设置K线值
            kLine.setOpenPrice(exchangeTrade.getPrice());
            kLine.setHighestPrice(exchangeTrade.getPrice());
            kLine.setLowestPrice(exchangeTrade.getPrice());
            kLine.setClosePrice(exchangeTrade.getPrice());
        } else {
            kLine.setHighestPrice(exchangeTrade.getPrice().max(kLine.getHighestPrice()));
            kLine.setLowestPrice(exchangeTrade.getPrice().min(kLine.getLowestPrice()));
            kLine.setClosePrice(exchangeTrade.getPrice());
        }
        kLine.setCount(kLine.getCount() + 1);
        kLine.setVolume(kLine.getVolume().add(exchangeTrade.getAmount()));
        BigDecimal turnover = exchangeTrade.getPrice().multiply(exchangeTrade.getAmount());
        kLine.setTurnover(kLine.getTurnover().add(turnover));
    }

    // 处理周K线和月K线的更具效率的方法
    public void processKline(KLine kline, long fromTime, long endTime, int field) {
        // 查询过去时间段的日线（7条）
        List<KLine> lines = marketService.findAllKLine(symbol, fromTime, endTime, "1day");
        if (lines.size() > 0) {
            kline.setOpenPrice(lines.get(0).getOpenPrice()); // 开盘价设置为首日开盘价
            kline.setLowestPrice(lines.get(0).getLowestPrice());
            for (KLine item : lines) {
                kline.setHighestPrice(kline.getHighestPrice().max(item.getHighestPrice()));
                kline.setLowestPrice(kline.getLowestPrice().min(item.getLowestPrice()));
                kline.setVolume(kline.getVolume().add(item.getVolume()));
                kline.setTurnover(kline.getTurnover().add(item.getTurnover()));
                kline.setCount(kline.getCount() + item.getCount());
            }
            kline.setClosePrice(lines.get(lines.size() - 1).getClosePrice()); // 收盘价设置为最后一日收盘价
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public KLine getCurrentKLine() {
        return currentKLine;
    }

    public void setCurrentKLine(KLine currentKLine) {
        this.currentKLine = currentKLine;
    }

    public List<MarketHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<MarketHandler> handlers) {
        this.handlers = handlers;
    }


    @Override
    public void setMarketService(MarketService marketService) {
        this.marketService = marketService;
    }

    public CoinThumb getCoinThumb() {
        return coinThumb;
    }

    public void setCoinThumb(CoinThumb coinThumb) {
        this.coinThumb = coinThumb;
    }

    public MarketService getMarketService() {
        return marketService;
    }

    public CoinExchangeRate getCoinExchangeRate() {
        return coinExchangeRate;
    }

    public Boolean getHalt() {
        return isHalt;
    }

    public void setHalt(Boolean halt) {
        isHalt = halt;
    }

    public Boolean getStopKLine() {
        return stopKLine;
    }

    public void setStopKLine(Boolean stopKLine) {
        this.stopKLine = stopKLine;
    }
}
