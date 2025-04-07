package com.watoukuang.altxch.exchange.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.watoukuang.altxch.common.enums.YnEnum;
import com.watoukuang.altxch.common.exception.ServiceException;
import com.watoukuang.altxch.common.response.R;
import com.watoukuang.altxch.core.dao.dataobj.ExchangeOrderDO;
import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.entity.Coin;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.dao.entity.Member;
import com.watoukuang.altxch.core.domain.dto.PageOrderDTO;
import com.watoukuang.altxch.core.enums.OrderDirectionEnum;
import com.watoukuang.altxch.core.enums.ExchangeOrderTypeEnum;
import com.watoukuang.altxch.exchange.domain.OrderDTO;
import com.watoukuang.altxch.core.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RestController
@RequestMapping(value = "order")
@RequiredArgsConstructor
public class ExchangeOrderController {
    private final MemberService memberService;
    private final CoinService coinService;
    private final ExchangeCoinService exchangeCoinService;
    private final MemberWalletService memberWalletService;
    private final ExchangeOrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 当前委托订单
     */
    @PostMapping(value = "pageCurrentOrder")
    public R<PageInfo<ExchangeOrderDO>> pageCurrentOrder(@RequestBody PageOrderDTO pageOrderDTO) {
        return R.ok(orderService.pageCurrentOrder(pageOrderDTO));
    }

    @PostMapping(value = "pageHistoryOrder")
    public R<PageInfo<ExchangeOrderDO>> pageHistoryOrder(@RequestBody PageOrderDTO pageOrderDTO) {
        return R.ok(orderService.pageHistoryOrder(pageOrderDTO));
    }

    @RequestMapping(value = "add")
    public R<Void> addOrder(@RequestBody OrderDTO orderDTO) throws ServiceException {
        // 验证订单参数
        validateOrderParameters(orderDTO);
        Long memberId = 100L; // TODO: 查询会员
        Member member = memberService.getById(memberId);
        // TODO 检查会员是否被禁止交易
        checkMemberTradePermission(member);
        // 验证并获取交易对信息
        ExchangeCoin exchangeCoin = validateAndRetrieveExchangeCoin(orderDTO.getSymbol());
        // 检查交易对的交易权限
        checkTradePermissions(exchangeCoin, orderDTO);
        // 验证价格的有效性
        BigDecimal price = validatePrice(orderDTO, exchangeCoin);
        // 验证数量的有效性
        BigDecimal amount = validateAmount(orderDTO, exchangeCoin);
        // 根据交易方向获取币种信息
        Coin coin = retrieveCoin(orderDTO.getDirection(), exchangeCoin);
        // 设置价格精度
        price = price.setScale(exchangeCoin.getBaseCoinScale(), RoundingMode.DOWN);
        // 处理市价单的逻辑
        amount = handleMarketOrder(orderDTO, amount, exchangeCoin);
        // 检查用户的钱包状态
        checkWalletAvailability(memberId, exchangeCoin);
        // 验证订单价格的合法性
        validateOrderPrice(orderDTO, exchangeCoin, price);
        // 检查是否允许市价买卖
        checkMarketOrderAvailability(orderDTO, exchangeCoin);
        // 检查是否达到最大交易数量
        checkMaxTradingOrder(member, orderDTO, exchangeCoin);
        // 创建新的订单对象
        ExchangeOrder order = createOrder(orderDTO, member, price, amount, exchangeCoin);
        // 将订单信息发送到消息队列
        orderService.addOrder(member.getId(), order);
        kafkaTemplate.send("exchange-order", JSON.toJSONString(order));
        return R.ok();
    }

    // 验证订单参数的合法性
    private void validateOrderParameters(OrderDTO orderDTO) throws ServiceException {
        if (orderDTO.getDirection() == null || orderDTO.getOrderType() == null) {
            throw new ServiceException("非法参数!");
        }
    }

    // 检查会员是否被禁止交易
    private void checkMemberTradePermission(Member member) throws ServiceException {
        if (member.getIsTrade().equals(YnEnum.YES.getCode())) {
            throw new ServiceException("当前用户禁止交易!");
        }
    }


    /**
     * 验证并获取交易对信息
     *
     * @param symbol
     * @return
     * @throws ServiceException
     */
    private ExchangeCoin validateAndRetrieveExchangeCoin(String symbol) throws ServiceException {
        ExchangeCoin exchangeCoin = exchangeCoinService.findBySymbol(symbol);
        if (exchangeCoin == null) {
            throw new ServiceException("不支持的币种!");
        }
        if (exchangeCoin.getEnable() != 1 || exchangeCoin.getEnableTrade() != 1) {
            throw new ServiceException("币种已被禁止交易!");
        }
        return exchangeCoin;
    }

    // 检查交易对的交易权限
    private void checkTradePermissions(ExchangeCoin exchangeCoin, OrderDTO orderDTO) throws ServiceException {
        String direction = orderDTO.getDirection();

        if (exchangeCoin.getEnableSell().equals(YnEnum.NO.getCode()) && direction.equals(OrderDirectionEnum.SELL.getKey())) {
            throw new ServiceException("禁止卖出该币种!");
        }

        if (exchangeCoin.getEnableBuy().equals(YnEnum.NO.getCode()) && direction.equals(OrderDirectionEnum.BUY.getKey())) {
            throw new ServiceException("禁止买入该币种!");
        }
    }

    // 验证价格的有效性
    private BigDecimal validatePrice(OrderDTO orderDTO, ExchangeCoin exchangeCoin) throws ServiceException {
        BigDecimal price = orderDTO.getPrice();
        if (price.compareTo(BigDecimal.ZERO) <= 0 && orderDTO.getOrderType().equals(ExchangeOrderTypeEnum.LIMIT_PRICE.getKey())) {
            throw new ServiceException("价格不能小于或等于零，请输入有效的限价!");
        }
        return price;
    }

    // 验证数量的有效性
    private BigDecimal validateAmount(OrderDTO orderDTO, ExchangeCoin exchangeCoin) throws ServiceException {
        BigDecimal amount = orderDTO.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("数量输入不合法，请输入一个大于零的数量!");
        }
        return amount;
    }

    // 根据交易方向获取币种信息
    private Coin retrieveCoin(String direction, ExchangeCoin exchangeCoin) throws ServiceException {
        String baseCoin = exchangeCoin.getBaseSymbol();
        String exCoin = exchangeCoin.getCoinSymbol();
        Coin coin;
        if (direction.equals(OrderDirectionEnum.SELL.getKey())) {
            coin = coinService.findByUnit(exCoin);
        } else {
            coin = coinService.findByUnit(baseCoin);
        }
        if (coin == null) {
            throw new ServiceException("不支持的币种!");
        }

        return coin;
    }

    // 处理市价单的逻辑
    private BigDecimal handleMarketOrder(OrderDTO orderDTO, BigDecimal amount, ExchangeCoin exchangeCoin) throws ServiceException {
        if (orderDTO.getDirection().equals(OrderDirectionEnum.BUY.getKey()) && orderDTO.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey())) {
            amount = amount.setScale(exchangeCoin.getBaseCoinScale(), RoundingMode.DOWN);
            if (amount.compareTo(exchangeCoin.getMinTurnover()) < 0) {
                throw new ServiceException("最低成交额不足，最低要求为: " + exchangeCoin.getMinTurnover());
            }
        } else {
            amount = amount.setScale(exchangeCoin.getCoinScale(), RoundingMode.DOWN);
            checkVolumeConstraints(amount, exchangeCoin);
        }
        return amount;
    }

    // 检查成交量的约束条件
    private void checkVolumeConstraints(BigDecimal amount, ExchangeCoin exchangeCoin) throws ServiceException {
        if (exchangeCoin.getMaxVolume() != null && exchangeCoin.getMaxVolume().compareTo(BigDecimal.ZERO) != 0
                && exchangeCoin.getMaxVolume().compareTo(amount) < 0) {
            throw new ServiceException("超过最大成交量，最大限制为: " + exchangeCoin.getMaxVolume());
        }

        if (exchangeCoin.getMinVolume() != null && exchangeCoin.getMinVolume().compareTo(BigDecimal.ZERO) != 0
                && exchangeCoin.getMinVolume().compareTo(amount) > 0) {
            throw new ServiceException("成交量过小，最低要求为: " + exchangeCoin.getMinVolume());
        }
    }

    // 检查用户的钱包状态
    private void checkWalletAvailability(Long memberId, ExchangeCoin exchangeCoin) throws ServiceException {
        String baseCoin = exchangeCoin.getBaseSymbol();
        String exCoin = exchangeCoin.getCoinSymbol();
        MemberWalletDO baseCoinWallet = memberWalletService.findByCoinUnitAndMemberId(baseCoin, memberId);
        MemberWalletDO exCoinWallet = memberWalletService.findByCoinUnitAndMemberId(exCoin, memberId);

        if (baseCoinWallet == null || exCoinWallet == null) {
            throw new ServiceException("不支持的币种!");
        }
        if (baseCoinWallet.getIsLock().equals(YnEnum.YES.getCode()) || exCoinWallet.getIsLock().equals(YnEnum.YES.getCode())) {
            throw new ServiceException("钱包已锁定，无法进行交易!");
        }
    }

    // 验证订单价格的合法性
    private void validateOrderPrice(OrderDTO orderDTO, ExchangeCoin exchangeCoin, BigDecimal price) throws ServiceException {
        if (orderDTO.getDirection().equals(OrderDirectionEnum.SELL.getKey()) && exchangeCoin.getMinSellPrice().compareTo(BigDecimal.ZERO) > 0
                && (price.compareTo(exchangeCoin.getMinSellPrice()) < 0 || orderDTO.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey()))) {
            throw new ServiceException("价格低于最低卖价，最低要求为: " + exchangeCoin.getMinSellPrice());
        }

        if (orderDTO.getDirection().equals(OrderDirectionEnum.BUY.getKey()) && exchangeCoin.getMaxBuyPrice().compareTo(BigDecimal.ZERO) > 0
                && (price.compareTo(exchangeCoin.getMaxBuyPrice()) > 0 || orderDTO.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey()))) {
            throw new ServiceException("价格超过最高买价，最高限制为: " + exchangeCoin.getMaxBuyPrice());
        }
    }

    // 检查是否允许市价买卖
    private void checkMarketOrderAvailability(OrderDTO orderDTO, ExchangeCoin exchangeCoin) throws ServiceException {
        if (orderDTO.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey())) {
            Integer enableMarketBuy = exchangeCoin.getEnableMarketBuy();
            Integer enableMarketSell = exchangeCoin.getEnableMarketSell();

            if (enableMarketBuy.equals(YnEnum.NO.getCode()) && orderDTO.getDirection().equals(OrderDirectionEnum.BUY.getKey())) {
                throw new ServiceException("不支持市价买入!");
            } else if (enableMarketSell.equals(YnEnum.NO.getCode()) && orderDTO.getDirection().equals(OrderDirectionEnum.SELL.getKey())) {
                throw new ServiceException("不支持市价卖出!");
            }
        }
    }

    // 检查是否达到最大交易数量
    private void checkMaxTradingOrder(Member member, OrderDTO orderDTO, ExchangeCoin exchangeCoin) throws ServiceException {
//        if (exchangeCoin.getMaxTradingOrder() > 0 && orderService.findCurrentTradingCount(member.getId(), orderDTO.getSymbol(), orderDTO.getDirection()) >= exchangeCoin.getMaxTradingOrder()) {
//            throw new ServiceException("已达到最大交易数量，最多允许交易: " + exchangeCoin.getMaxTradingOrder());
//        }
    }

    // 创建新的订单对象
    private ExchangeOrder createOrder(OrderDTO orderDTO, Member member, BigDecimal price, BigDecimal amount, ExchangeCoin exchangeCoin) {
        ExchangeOrder order = new ExchangeOrder();
        order.setMemberId(member.getId());
        order.setSymbol(orderDTO.getSymbol());
        order.setBaseSymbol(exchangeCoin.getBaseSymbol());
        order.setCoinSymbol(exchangeCoin.getCoinSymbol());
        order.setOrderType(orderDTO.getOrderType());
        order.setDirection(orderDTO.getDirection());
        order.setPrice(order.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey()) ? BigDecimal.ZERO : price);
        order.setUseDiscount("0");
        order.setAmount(amount);
        return order;
    }
}
