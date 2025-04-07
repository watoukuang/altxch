package com.watoukuang.altxch.member.client;

import com.watoukuang.altxch.common.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "altxch-wallet")
public interface WalletClient {

    @GetMapping(value = "/wallet/getAddress")
    R<String> getAddress(@RequestParam("protocolName") String protocolName, @RequestParam("account") String account);
}
