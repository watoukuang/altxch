package com.watoukuang.altxch.wallet.controller;

import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.wallet.client.BtcNodeClient;
import com.watoukuang.altxch.wallet.dao.entity.Wallet;
import com.watoukuang.altxch.wallet.exception.ServiceException;
import com.watoukuang.altxch.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "wallet")
public class WalletController {

    private final WalletService walletService;
    private final BtcNodeClient btcNodeClient;

    @PostMapping
    public boolean save(@RequestBody Wallet wallet) {
        return walletService.save(wallet);
    }

    @PutMapping
    public boolean update(@RequestBody Wallet wallet) {
        return walletService.updateById(wallet);
    }

    @DeleteMapping(value = "{id}")
    public boolean delete(@PathVariable Long id) {
        return walletService.removeById(id);
    }

    @GetMapping(value = "{id}")
    public Wallet getById(@PathVariable Long id) {
        return walletService.getById(id);
    }

    @GetMapping(value = "list")
    public List<Wallet> list() {
        return walletService.list();
    }

    @GetMapping(value = "getHeight")
    public R<Integer> getHeight() throws ServiceException {
        R<Integer> rtp = btcNodeClient.getHeight();
        int code = rtp.getCode();
        if (code != 200) {
            throw new ServiceException("获取节点高度失败!");
        }
        return R.ok(rtp.getData());
    }

    @GetMapping(value = "getAddress")
    public R<Object> getAddress(@RequestParam("protocolName") String protocolName, @RequestParam("account") String account)
            throws ServiceException {
        R<String> rtp = btcNodeClient.getNewAddress(account);
        int code = rtp.getCode();
        if (code != 200) {
            throw new ServiceException("获取地址失败!");
        }
        return R.ok(rtp.getData());
    }

    @GetMapping(value = "transfer")
    public R<String> transfer() throws ServiceException {
//        Result<String> rtp = btcNodeClient.transfer();
//        int code = rtp.getCode();
//        if (code != 200) {
//            throw new ServiceException("获取地址失败!");
//        }
//        return Result.ok(rtp.getData());
        return null;
    }


    @GetMapping(value = "balance")
    public R<BigDecimal> balance() throws ServiceException {
        return null;
    }

    @GetMapping(value = "balance/{address}")
    public R<BigDecimal> balance(@PathVariable String address) {
        return null;
    }
}
