package com.watoukuang.altxch.exchange.client;

import com.watoukuang.altxch.common.response.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "altxch-match")
public interface AltxchMatchClient {
    @GetMapping(value = "/monitor/getTradePlate")
    R<Object> getTraderPlate();

    @GetMapping(value = "/monitor/getTradePlateMini")
    R<Object> getTradePlateMini();
}
