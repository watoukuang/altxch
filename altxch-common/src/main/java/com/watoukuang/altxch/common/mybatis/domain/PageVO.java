package com.watoukuang.altxch.common.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 数据返回列表
     */
    private List<T> list;

    /**
     * 总记录数
     */
    private long total;

}
