package com.watoukuang.altxch.member.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.member.dao.entity.AddressExt;
import com.watoukuang.altxch.member.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "address")
public class AddressController {
    private final AddressService addressService;

    /**
     * 创建地址
     */
    @PostMapping(value = "create")
    public R<AddressExt> create(@RequestParam(value = "protocol") Integer protocol) {
        Long memberId = 100L;
        return R.ok(addressService.create(memberId, protocol));
    }

}
