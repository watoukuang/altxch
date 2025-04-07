package com.watoukuang.altxch.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.watoukuang.altxch.common.enums.YnEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "t_exchange_coin")
public class ExchangeCoin implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 交易币
     */
    private String coinSymbol;

    /**
     * 结算币种符号，如USDT
     */
    private String baseSymbol;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 交易币小数精度
     */
    private int coinScale;

    /**
     * 基币小数精度
     */
    private int baseCoinScale;

    /**
     * 最低卖单价
     */
    private BigDecimal minSellPrice;

    /**
     * 最高买单价
     */
    private BigDecimal maxBuyPrice;

    /**
     * 最小下单量，0表示不限制
     */
    private BigDecimal minVolume = BigDecimal.ZERO;

    /**
     * 最大下单量，0表示不限制
     */
    private BigDecimal maxVolume = BigDecimal.ZERO;

    /**
     * 交易区域
     */
    private int zone = 0;

    /**
     * 最小挂单额
     */
    private BigDecimal minTurnover;
    /**
     * 排序
     */
    private int sort;

    /**
     * 发行活动类型 1：无活动，2：抢购发行，3：分摊发行
     */
    private Integer publishType = 1;

    /**
     * 活动开始时间(抢购发行与分摊发行都需要设置)
     */
    private String startTime;

    /**
     * 活动结束时间(抢购发行与分摊发行都需要设置)
     */
    private String endTime;

    /**
     * 活动清盘时间(抢购发行与分摊发行都需要设置)
     */
    private String clearTime;

    /**
     * 分摊发行价格(抢购发行与分摊发行都需要设置)
     */
    private BigDecimal publishPrice;

    /**
     * 发行数量(抢购发行与分摊发行都需要设置)
     */
    private BigDecimal publishAmount;

    /**
     * 状态
     */
    private int enable;

    /**
     * 前台可见状态，1：可见，2：不可见
     */
    private int visible;
    /**
     * 是否可交易(1：可交易，2：不可交易)
     */
    private Integer enableTrade;

    /**
     * 是否启用市价卖
     */
    private Integer enableMarketSell = YnEnum.YES.getCode();

    /**
     * 是否启用市价买
     */
    private Integer enableMarketBuy = YnEnum.YES.getCode();

    /**
     * 是否允许卖
     */
    private Integer enableSell = YnEnum.YES.getCode();

    /**
     * 是否允许买
     */
    private Integer enableBuy = YnEnum.YES.getCode();

    /**
     * 机器人类型
     * 0：一般机器人，适用于有外部市场价格做参考
     * 1：控盘机器人，适用于只有一个价格的机器人
     */
    private int robotType;

    /**
     * 假数据状态
     */
    private Integer fakeDataState;
}
