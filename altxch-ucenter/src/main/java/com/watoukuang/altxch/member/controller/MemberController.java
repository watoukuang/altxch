package com.watoukuang.altxch.member.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.dto.MemberRegDTO;
import com.watoukuang.altxch.core.service.MemberService;
import com.watoukuang.altxch.core.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "reg")
    public R<Void> reg(@RequestBody MemberRegDTO regDTO) {
        memberService.reg(regDTO);
        return R.ok();
    }
}
