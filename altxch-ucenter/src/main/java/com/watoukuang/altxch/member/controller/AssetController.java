package com.watoukuang.altxch.member.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "asset")
public class AssetController {
    private final WalletService walletService;

    @GetMapping(value = "wallet/{symbol}")
    public R<Object> findWalletBySymbol(@PathVariable String symbol) {
        Long memberId = 100L;
        return R.ok(walletService.findByCoinUnitAndMemberId(symbol, memberId));
    }
}
