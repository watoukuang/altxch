package com.watoukuang.altxch.wallet.client;

import com.watoukuang.altxch.common.response.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "altxch-node-btc")
public interface BtcNodeClient {

    @GetMapping(value = "node/getHeight")
    R<Integer> getHeight();

    @GetMapping(value = "node/address/{account}")
    R<String> getNewAddress(@PathVariable String account);

    @PostMapping(value = "/transfer")
    R<String> sendTransaction(@RequestParam("address") String address, @RequestParam("amount") BigDecimal amount, @RequestParam("fee") BigDecimal fee);

    @GetMapping(value = "/balance")
    R<BigDecimal> getBalance();

    @GetMapping(value = "/balance")
    BigDecimal getBalance(String account);


}
