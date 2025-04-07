package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublishTypeEnum {
    UNKNOWN(0, "UNKNOWN"),
    NONE(1, "NONE"),
    QIANGGOU(2, "QIANGGOU"),
    FENTAN(3, "FENTAN");

    private final Integer code;
    private final String description;

}
