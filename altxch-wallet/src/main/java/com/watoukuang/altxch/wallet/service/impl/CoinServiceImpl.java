package com.watoukuang.altxch.wallet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watoukuang.altxch.wallet.dao.entity.Coin;
import com.watoukuang.altxch.wallet.dao.mapper.CoinMapper;
import com.watoukuang.altxch.wallet.service.CoinService;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {
}
