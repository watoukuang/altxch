package com.watoukuang.altxch.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_coin")
public class Coin implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 货币
     */
    private String name;

    /**
     * 缩写
     */
    private String unit;
}
