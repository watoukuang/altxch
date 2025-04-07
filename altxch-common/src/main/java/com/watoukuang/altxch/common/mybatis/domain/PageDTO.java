package com.watoukuang.altxch.common.mybatis.domain;

import com.watoukuang.altxch.common.mybatis.annotation.Query;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页对象
 */
@Data
public class PageDTO implements Serializable {
    private static final Long serialVersionUID = 1L;

    /**
     * 每页显示记录数
     */
    @Query(ignore = true)
    private Long pageSize;

    @Query(ignore = true)
    private Long pageNum;

    @Query(ignore = true)
    private String orderColumn;

    @Query(ignore = true)
    private String orderType;
}
