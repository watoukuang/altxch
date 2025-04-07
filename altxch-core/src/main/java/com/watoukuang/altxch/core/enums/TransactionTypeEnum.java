package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {
    RECHARGE("RECHARGE", "充值"),
    WITHDRAW("WITHDRAW", "提现"),
    TRANSFER_ACCOUNTS("TRANSFER_ACCOUNTS", "转账"),
    EXCHANGE("EXCHANGE", "币币交易"),
    OTC_BUY("OTC_BUY", "法币买入"),
    OTC_SELL("OTC_SELL", "法币卖出"),
    ACTIVITY_AWARD("ACTIVITY_AWARD", "活动奖励"),
    PROMOTION_AWARD("PROMOTION_AWARD", "推广奖励"),
    DIVIDEND("DIVIDEND", "分红"),
    VOTE("VOTE", "投票"),
    ADMIN_RECHARGE("ADMIN_RECHARGE", "人工充值"),
    MATCH("MATCH", "配对"),
    ACTIVITY_BUY("ACTIVITY_BUY", "活动兑换"),
    CTC_BUY("CTC_BUY", "CTC买入"),
    CTC_SELL("CTC_SELL", "CTC卖出"),
    RED_OUT("RED_OUT", "红包发出"),
    RED_IN("RED_IN", "红包领取"),
    WITHDRAW_CODE_OUT("WITHDRAW_CODE_OUT", "提现码提现"),
    WITHDRAW_CODE_IN("WITHDRAW_CODE_IN", "提现码充值"),
    CONTRACT_FEE("CONTRACT_FEE", "永续合约手续费"),
    CONTRACT_PROFIT("CONTRACT_PROFIT", "永续合约盈利"),
    CONTRACT_LOSS("CONTRACT_LOSS", "永续合约亏损"),
    OPTION_FAIL("OPTION_FAIL", "期权合约失败"),
    OPTION_FEE("OPTION_FEE", "期权合约手续费"),
    OPTION_REWARD("OPTION_REWARD", "期权合约奖金"),
    CONTRACT_AWARD("CONTRACT_AWARD", "合约返佣"),
    LEVEL_AWARD("LEVEL_AWARD", "平级奖励"),
    PLATFORM_FEE_AWARD("PLATFORM_FEE_AWARD", "平台手续费收入"),
    SECOND_FAIL("SECOND_FAIL", "秒合约失败"),
    SECOND_REWARD("SECOND_REWARD", "秒合约奖金"),
    FINANCE_REWARD("FINANCE_REWARD", "理财利息"),
    PAY_CHARGE_FEE("PAY_CHARGE_FEE", "支出资金费用"),
    GET_CHARGE_FEE("GET_CHARGE_FEE", "获得资金费用"),
    AUTO_INVEST_BUY("AUTO_INVEST_BUY", "定投买入"),
    AUTO_INVEST_SELL("AUTO_INVEST_SELL", "定投卖出"),
    LOCKED_SAVING_BUY("LOCKED_SAVING_BUY", "购买定期"),
    LOCKED_SAVING_SELL("LOCKED_SAVING_SELL", "定期赎回"),
    TRANSFER_IN_COIN("TRANSFER_IN_COIN", "币本位合约划转转入"),
    TRANSFER_OUT_COIN("TRANSFER_OUT_COIN", "币本位合约划转转出"),
    TRANSFER_IN_USDT("TRANSFER_IN_USDT", "U本位合约划转转入"),
    TRANSFER_OUT_USDT("TRANSFER_OUT_USDT", "U本位合约划转转入"),
    TRANSFER_IN_SECOND("TRANSFER_IN_SECOND", "秒合约划转转入"),
    TRANSFER_OUT_SECOND("TRANSFER_OUT_SECOND", "秒合约划转转入"),
    TRANSFER_IN("TRANSFER_IN", "币币划转转入"),
    TRANSFER_OUT("TRANSFER_OUT", "币币划转转出");

    /**
     * 唯一标识符
     */
    private final String key;
    /**
     * 描述
     */
    private final String description;

    /**
     * 根据 key 获取枚举实例
     *
     * @param key 唯一标识符
     * @return 对应的枚举实例，如果未找到则返回 null
     */
    public static TransactionTypeEnum getByKey(String key) {
        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null;
    }
}