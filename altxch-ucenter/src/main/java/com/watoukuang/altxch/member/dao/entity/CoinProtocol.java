package com.watoukuang.altxch.member.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_coin_protocol")
public class CoinProtocol {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 协议类型的编号
     */
    private Integer protocol;

    /**
     * 协议名称
     */
    private String protocolName;

    /**
     * RPC服务器地址
     */
    private String rpcServer;

    /**
     * RPC服务器用户名
     */
    private String rpcUser;

    /**
     * 服务器的密码
     */
    private String rpcPassword;

    /**
     * 浏览器类型或支持的浏览器
     */
    private String browser;

    /**
     * 货币的符号(例如BTC,ETH)
     */
    private String symbol;

    /**
     * 区块链的唯一标识符（链ID）
     */
    private Integer chainId;
}
