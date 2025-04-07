package com.watoukuang.altxch.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watoukuang.altxch.member.dao.entity.AddressExt;
import com.watoukuang.altxch.member.dao.mapper.AddressExtMapper;
import com.watoukuang.altxch.member.service.AddressExtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressExtServiceImpl extends ServiceImpl<AddressExtMapper, AddressExt> implements AddressExtService {

}
