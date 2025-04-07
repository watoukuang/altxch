package com.watoukuang.altxch.core.service.impl;

import com.watoukuang.altxch.common.enums.YnEnum;
import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.core.dao.mapper.MemberMapper;
import com.watoukuang.altxch.core.dao.entity.Member;
import com.watoukuang.altxch.core.domain.dto.MemberRegDTO;
import com.watoukuang.altxch.core.service.MemberService;
import org.springframework.stereotype.Service;

@Service
class MemberServiceImpl extends BaseServiceImpl<MemberMapper, Member> implements MemberService {
    private MemberMapper memberMapper;

    @Override
    public void reg(MemberRegDTO memberRegDTO) {
        String email = memberRegDTO.getEmail();
        String captcha = memberRegDTO.getCaptcha();
        String passwd = memberRegDTO.getPasswd();
        Member member = new Member();
        member.setEmail(email);
        member.setUsername(email);
        member.setPasswd(passwd);
        member.setIsTrade(YnEnum.YES.getCode());
        memberMapper.insert(member);
    }
}