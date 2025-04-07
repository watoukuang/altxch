package com.watoukuang.altxch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YnEnum {

    /**
     * 否
     */
    NO(0, false, "否"),
    /**
     * 是
     */
    YES(1, true, "是");

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 布尔值
     */
    private final boolean value;
    /**
     * 中文描述
     */
    private final String zh;
}