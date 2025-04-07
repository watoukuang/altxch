package com.watoukuang.altxch.member.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_address_ext") // 指定表名
public class AddressExt {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO) // 自增主键
    private Integer id;

    /**
     * 地址
     */
    private String address;

    /**
     * 币种协议
     */
    private Integer coinProtocol;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 状态
     */
    private Integer status;
}
