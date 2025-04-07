package com.watoukuang.altxch.wallet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watoukuang.altxch.wallet.dao.entity.Wallet;
import com.watoukuang.altxch.wallet.dao.mapper.WalletMapper;
import com.watoukuang.altxch.wallet.service.WalletService;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {
    @Override
    public void createWallet() {

    }

    @Override
    public void generatorWalletAddress() {

    }

    @Override
    public void transfer() {

    }


}
