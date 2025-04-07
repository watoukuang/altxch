package com.watoukuang.altxch.wallet.controller;

import com.watoukuang.altxch.wallet.dao.entity.Coin;
import com.watoukuang.altxch.wallet.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "coin")
public class CoinController {
    private final CoinService coinService;

    /**
     * 新增币种
     */
    @PostMapping
    public boolean save(@RequestBody Coin coin) {
        return coinService.save(coin);
    }

    /**
     * 更新币种
     */
    @PutMapping
    public boolean update(@RequestBody Coin coin) {
        return coinService.updateById(coin);
    }

    /**
     * 删除币种
     */
    @DeleteMapping(value = "{id}")
    public boolean delete(@PathVariable Long id) {
        return coinService.removeById(id);
    }

    /**
     * 根据ID查询币种
     */
    @GetMapping(value = "{id}")
    public Coin getById(@PathVariable Long id) {
        return coinService.getById(id);
    }

    /**
     * 查询所有币种
     */
    @GetMapping(value = "list")
    public List<Coin> list() {
        return coinService.list();
    }
}
