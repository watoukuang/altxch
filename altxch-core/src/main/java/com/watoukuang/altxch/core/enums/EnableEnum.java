package com.watoukuang.altxch.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableEnum {
    ENABLED(1, "启用"),

    DISABLED(2, "禁止");

    private final int code;

    private final String description;
}
