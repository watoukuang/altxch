package com.watoukuang.altxch.common.mybatis.utils;

import com.watoukuang.altxch.common.mybatis.domain.PageDTO;
import com.watoukuang.altxch.common.mybatis.domain.PageVO;
import com.watoukuang.altxch.common.mybatis.property.PageProperty;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils {
    /**
     * 开始分页
     */
    public static void startPage(PageDTO pageDTO) {
        startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
    }

    /**
     * 开始分页
     *
     * @param pageNum  起始页
     * @param pageSize 分页记录数
     */
    public static void startPage(Long pageNum, Long pageSize) {
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
        PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
    }


    /**
     * 获取分页信息
     */
    public static <T> PageVO<T> getPage(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageVO<>(list, pageInfo.getTotal());
    }

}
