package com.watoukuang.altxch.member.controller;

import com.watoukuang.altxch.member.dao.entity.CoinProtocol;
import com.watoukuang.altxch.member.service.CoinProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "coinProtocol")
public class CoinProtocolController {
    private final CoinProtocolService coinProtocolService;

    @PostMapping(value = "save")
    public boolean save(@RequestBody CoinProtocol coinProtocol) {
        return coinProtocolService.save(coinProtocol);
    }
}
