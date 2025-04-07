package com.watoukuang.altxch.wallet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.watoukuang.altxch.wallet.dao.entity.Wallet;

public interface WalletService extends IService<Wallet> {

    /**
     * 创建钱包
     */
    void createWallet();

    /**
     * 生产钱包地址
     */
    void generatorWalletAddress();

    void transfer();
}
