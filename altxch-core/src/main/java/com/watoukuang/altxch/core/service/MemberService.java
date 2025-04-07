package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.common.mybatis.service.BaseService;
import com.watoukuang.altxch.core.dao.entity.Member;
import com.watoukuang.altxch.core.domain.dto.MemberRegDTO;

public interface MemberService extends BaseService<Member> {

    /**
     * 用户注册
     */
    void reg(MemberRegDTO regDTO);
}
