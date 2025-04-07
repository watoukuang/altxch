package com.watoukuang.altxch.member.service;

import com.watoukuang.altxch.member.dao.entity.AddressExt;

public interface AddressService {
    AddressExt create(Long memberId, Integer protocol);
}
