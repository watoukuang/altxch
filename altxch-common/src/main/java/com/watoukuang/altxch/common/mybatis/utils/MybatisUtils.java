package com.watoukuang.altxch.common.mybatis.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watoukuang.altxch.common.mybatis.domain.PageDTO;
import com.watoukuang.altxch.common.mybatis.property.PageProperty;

/**
 * MyBatis 工具类
 */
public class MybatisUtils {

    /**
     * 构建分页
     */
    public static <T> Page<T> buildPage(PageDTO pageDTO) {
        Long pageNum = null;
        Long pageSize = null;
        if (pageDTO != null) {
            pageNum = pageDTO.getPageNum();
            pageSize = pageDTO.getPageSize();
        }
        return buildPage(pageNum, pageSize);
    }

    /**
     * 构建分页
     */
    public static <T> Page<T> buildPage(Long pageNum, Long pageSize) {
        // 如果为空，则设置默认值
        if (pageNum == null) {
            pageNum = 1L;
        }
        if (pageSize == null) {
            pageSize = PageProperty.defaultPageSize;
        }

        // 如果超过最大分页数，则设置为最大分页数
        if (pageSize > PageProperty.maxPageSize) {
            pageSize = PageProperty.maxPageSize;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        return page;
    }

}
