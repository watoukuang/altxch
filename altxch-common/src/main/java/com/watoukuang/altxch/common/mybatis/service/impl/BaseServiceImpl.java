package com.watoukuang.altxch.common.mybatis.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watoukuang.altxch.common.mybatis.dao.BaseMapperPlus;
import com.watoukuang.altxch.common.mybatis.domain.PageVO;
import com.watoukuang.altxch.common.mybatis.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 在 MyBatis Plus 的 ServiceImpl 的基础上拓展，后续提供更多功能
 */
public class BaseServiceImpl<M extends BaseMapperPlus<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
    @Autowired
    private M baseMapper;

    /**
     * 查询分页
     *
     * @param d             实体类参数对接
     * @param selectColumns 查询返回的列
     */
    @Override
    public <D> PageVO<T> page(D d, SFunction<T, ?>... selectColumns) {
        return baseMapper.selectPage(d, selectColumns);
    }
}
