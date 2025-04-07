package com.watoukuang.altxch.admin.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.domain.dto.ExchangeCoinDTO;
import com.watoukuang.altxch.core.domain.dto.PageExchangeCoinDTO;
import com.watoukuang.altxch.core.service.ExchangeCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "exchangeCoin")
@RequiredArgsConstructor
public class ExchangeCoinController {
    private final ExchangeCoinService exchangeCoinService;

    @PostMapping(value = "addExchangeCoin")
    public R<Void> addExchangeCoin(@RequestBody ExchangeCoinDTO exchangeCoinDTO) {
        exchangeCoinService.addExchangeCoin(exchangeCoinDTO);
        return R.ok();
    }

    @PostMapping(value = "pageExchangeCoins")
    public R<Object> pageExchangeCoins(@RequestBody PageExchangeCoinDTO pageExchangeCoinDTO) {
        return R.ok(exchangeCoinService.pageExchangeCoins(pageExchangeCoinDTO));
    }

}
