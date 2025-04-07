package com.watoukuang.altxch.wallet.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 币种实体类
 */
@Data
@TableName("t_coin")
public class Coin {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 币种名称
     */
    private String name;
    /**
     * 图标URL
     */
    private String iconUrl;
    /**
     * 是否支持自动提币（0：否，1：是）
     */
    private Integer canAutoWithdraw;
    /**
     * 是否支持充值（0：否，1：是）
     */
    private Integer canRecharge;
    /**
     * 是否支持转账（0：否，1：是）
     */
    private Integer canTransfer;
    /**
     * 是否支持提币（0：否，1：是）
     */
    private Integer canWithdraw;
    /**
     * 人命币汇率
     */
    private BigDecimal cnyRate;
    /**
     * 是否启用RPC（0：否，1：是）
     */
    private Integer enableRpc;
    /**
     * 是否为平台币（0：否，1：是）
     */
    private Integer isPlatformCoin;
    /**
     * 最大交易手续费
     */
    private BigDecimal maxTxFee;

    /**
     * 最大提币数量
     */
    private BigDecimal maxWithdrawAmount;
    /**
     * 最小交易手续费
     */
    private BigDecimal minTxFee;
    /**
     * 最小提笔数
     */
    private BigDecimal minWithdrawAmount;
    /**
     * 币种中文名称
     */
    private String nameCn;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;
    /**
     * 币种单位
     */
    private String unit;
    /**
     * 美元汇率
     */
    private BigDecimal usdRate;
    /**
     * 提现阈值
     */
    private BigDecimal withdrawThreshold;
    /**
     * 是否法币
     */
    private Integer hasLegal;
    /**
     * 冷钱包地址
     */
    private String coldWalletAddress;
    /**
     * 矿工费
     */
    private BigDecimal minerFee;
    /**
     * 提笔精度
     */
    private Integer withdrawScale;
    /**
     * 币种信息
     */
    private String info;
    /**
     * 币种链接
     */
    private String infoLink;
    /**
     * 币种账户列表
     */
    private Integer accountType;
    /**
     * 充值地址
     */
    private String depositAddress;
    /**
     * 最小充值数量
     */
    private BigDecimal minRechargeAmount;
}