package com.watoukuang.altxch.common.mybatis.property;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 分页配置
 */
@Getter
@Configuration
public class PageProperty {

    /**
     * 默认分页记录数
     */
    public static Long defaultPageSize;

    /**
     * 最大分页记录数
     */
    public static Long maxPageSize;

    @Value("${page.default-page-size:10}")
    public void setDefaultPageSize(Long defaultPageSize) {
        PageProperty.defaultPageSize = defaultPageSize;
    }

    @Value("${page.max-page-size:200}")
    public void setMaxPageSize(Long maxPageSize) {
        PageProperty.maxPageSize = maxPageSize;
    }
}
