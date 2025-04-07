package com.watoukuang.altxch.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.watoukuang.altxch.common.enums.YnEnum;
import com.watoukuang.altxch.common.exception.ServiceException;
import com.watoukuang.altxch.common.mybatis.service.impl.BaseServiceImpl;
import com.watoukuang.altxch.common.mybatis.utils.PageUtils;
import com.watoukuang.altxch.common.utils.GeneratorUtil;
import com.watoukuang.altxch.core.dao.dataobj.ExchangeOrderDO;
import com.watoukuang.altxch.core.dao.dataobj.MemberWalletDO;
import com.watoukuang.altxch.core.dao.entity.ExchangeCoin;
import com.watoukuang.altxch.core.dao.entity.ExchangeOrder;
import com.watoukuang.altxch.core.dao.entity.Member;
import com.watoukuang.altxch.core.dao.entity.MemberTransaction;
import com.watoukuang.altxch.core.dao.mapper.*;
import com.watoukuang.altxch.core.dao.repository.ExchangeOrderDetailRepository;
import com.watoukuang.altxch.core.dao.repository.OrderDetailAggregationRepository;
import com.watoukuang.altxch.core.domain.ExchangeOrderDetail;
import com.watoukuang.altxch.core.domain.ExchangeTrade;
import com.watoukuang.altxch.core.domain.OrderDetailAggregation;
import com.watoukuang.altxch.core.domain.dto.PageOrderDTO;
import com.watoukuang.altxch.core.enums.*;
import com.watoukuang.altxch.core.service.ExchangeOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeOrderServiceImpl extends BaseServiceImpl<ExchangeOrderMapper, ExchangeOrder> implements ExchangeOrderService {
    private final ExchangeOrderMapper exchangeOrderMapper;
    private final MemberWalletMapper memberWalletMapper;
    private final ExchangeCoinMapper exchangeCoinMapper;
    private final ExchangeOrderDetailRepository exchangeOrderDetailRepository;
    private final OrderDetailAggregationRepository orderDetailAggregationRepository;
    private final MemberMapper memberMapper;
    private final MemberTransactionMapper memberTransactionMapper;

    @Override
    public int findCurrentTradingCount(Long id, String symbol, Integer direction) {
        return 0;
    }

    @Override
    public void addOrder(Long memberId, ExchangeOrder order) {
        order.setTime(Calendar.getInstance().getTimeInMillis());
        order.setState(OrderStateEnum.TRADING.getKey());
        order.setTradedAmount(BigDecimal.ZERO);
        String orderId = GeneratorUtil.getOrderId("E");
        order.setOrderId(orderId);
        log.info("添加订单:{}", order);
        String direction = order.getDirection();
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            handleBuyOrder(memberId, order);
        } else if (direction.equals(OrderDirectionEnum.SELL.getKey())) {
            handleSellOrder(memberId, order);
        }
        exchangeOrderMapper.insert(order);
    }

    private void handleBuyOrder(Long memberId, ExchangeOrder order) {
        String baseSymbol = order.getBaseSymbol();
        MemberWalletDO wallet = memberWalletMapper.selectMemberWallet(baseSymbol, memberId);
        checkWalletLocked(wallet);

        BigDecimal turnover = calculateTurnover(order);
        validateBalance(wallet.getBalance(), turnover, order.getBaseSymbol());
        // TODO 调用钱包服务
//        MessageResult result = memberWalletService.freezeBalance(wallet, turnover);
//        if (result.getCode() != 0) {
//            throw new RuntimeException("余额不足: " + order.getBaseSymbol());
//        }
    }

    private void handleSellOrder(Long memberId, ExchangeOrder order) {
        String coinSymbol = order.getCoinSymbol();
        MemberWalletDO wallet = memberWalletMapper.selectMemberWallet(coinSymbol, memberId);
        checkWalletLocked(wallet);
        if (wallet.getBalance().compareTo(order.getAmount()) < 0) {
            throw new RuntimeException("余额不足: " + order.getCoinSymbol());
        } else {
            // 调用钱包服务
//            MessageResult result = memberWalletService.freezeBalance(wallet, order.getAmount());
//            if (result.getCode() != 0) {
//                throw new RuntimeException("余额不足: " + order.getCoinSymbol());
//            }
        }
    }

    private void checkWalletLocked(MemberWalletDO wallet) {
        if (wallet.getIsLock().equals(YnEnum.YES.getCode())) {
            throw new RuntimeException("钱包已锁定");
        }
    }

    private BigDecimal calculateTurnover(ExchangeOrder order) {
        if (order.getOrderType().equals(ExchangeOrderTypeEnum.MARKET_PRICE.getKey())) {
            return order.getAmount();
        } else {
            return order.getAmount().multiply(order.getPrice());
        }
    }

    private void validateBalance(BigDecimal balance, BigDecimal turnover, String baseSymbol) {
        if (balance.compareTo(turnover) < 0) {
            throw new RuntimeException("余额不足: " + baseSymbol);
        }
    }

    /**
     * 查询所有未完成的挂单
     */
    @Override
    public List<ExchangeOrder> findAllTradingOrderBySymbol(String symbol) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getSymbol, symbol).eq(ExchangeOrder::getState, OrderStateEnum.TRADING.getKey());
        return exchangeOrderMapper.selectList(queryWrapper);
    }

    @Override
    public void tradeCompleted(String orderId, BigDecimal tradedAmount, BigDecimal turnover) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getOrderId, orderId);
        ExchangeOrder order = exchangeOrderMapper.selectOne(queryWrapper);
        if (!order.getState().equals(OrderStateEnum.TRADING.getKey())) {
            throw new ServiceException("订单无效 (" + orderId + ")，当前状态不是交易中。");
        }
        order.setTradedAmount(tradedAmount);
        order.setTurnover(turnover);
        order.setState(OrderStateEnum.COMPLETED.getKey());
        order.setCompletedTime(Calendar.getInstance().getTimeInMillis());
        exchangeOrderMapper.updateById(order);
        // TODO 处理用户钱包,对冻结作处理，剩余成交额退回
//        orderRefund(order, tradedAmount, turnover);
    }


    @Override
    @Transactional
    public void processExchangeTrade(ExchangeTrade trade, boolean secondReferrerAward) throws Exception {
        log.info("processExchangeTrade,trade = {}", trade);
        if (trade == null || trade.getBuyOrderId() == null || trade.getSellOrderId() == null) {
            throw new ServiceException("交易记录为空!");
        }
        ExchangeOrder buyOrder = this.findOne(trade.getBuyOrderId());
        ExchangeOrder sellOrder = this.findOne(trade.getSellOrderId());
        if (buyOrder == null || sellOrder == null) {
            throw new ServiceException("交易订单不存在!");
        }
        // 获取手续费率
        ExchangeCoin coin = getExchangeCoin(buyOrder);
        if (coin == null) {
            throw new IllegalArgumentException("Invalid trade symbol: " + buyOrder.getSymbol());
        }
//        // 根据memberId锁表，防止死锁
//        DB.query("select id from member_wallet where member_id = ? for update;", buyOrder.getMemberId());
//        if (!buyOrder.getMemberId().equals(sellOrder.getMemberId())) {
//            DB.query("select id from member_wallet where member_id = ? for update;", sellOrder.getMemberId());
//        }
//        //处理买入订单 手续费 是交易币  交易币对usdtRat
        processOrder(buyOrder, trade, coin, secondReferrerAward);
//        //处理卖出订单 手续费是基准币 基准币对usdtRat
        processOrder(sellOrder, trade, coin, secondReferrerAward);
    }

    private ExchangeCoin getExchangeCoin(ExchangeOrder exchangeOrder) {
        LambdaQueryWrapper<ExchangeCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeCoin::getSymbol, exchangeOrder.getSymbol());
        return exchangeCoinMapper.selectOne(queryWrapper);
    }


    @Override
    public ExchangeOrder findOne(String orderId) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getOrderId, orderId);
        return exchangeOrderMapper.selectOne(queryWrapper);
    }

    @Override
    public PageInfo<ExchangeOrderDO> pageCurrentOrder(PageOrderDTO pageDTO) {
        PageUtils.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<ExchangeOrderDO> orders = exchangeOrderMapper.selectExchangeOrders(pageDTO);
        orders.forEach(exchangeOrderDO -> {
            BigDecimal tradedAmount = BigDecimal.ZERO;
            BigDecimal turnover = BigDecimal.ZERO;
            String orderId = exchangeOrderDO.getOrderId();
            List<ExchangeOrderDetail> details = exchangeOrderDetailRepository.findAllByOrderId(orderId);
            exchangeOrderDO.setDetail(details);
            for (ExchangeOrderDetail trade : details) {
                tradedAmount = tradedAmount.add(trade.getAmount());
                turnover = turnover.add(trade.getTurnover());
            }
            exchangeOrderDO.setTradedAmount(tradedAmount);
            exchangeOrderDO.setTurnover(turnover);
        });
        return new PageInfo<ExchangeOrderDO>(orders);
    }

    @Override
    public PageInfo<ExchangeOrderDO> pageHistoryOrder(PageOrderDTO pageDTO) {
        PageUtils.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<ExchangeOrderDO> orders = exchangeOrderMapper.selectExchangeOrders(pageDTO);
        orders.forEach(exchangeOrderDO -> {
            String orderId = exchangeOrderDO.getOrderId();
            List<ExchangeOrderDetail> details = exchangeOrderDetailRepository.findAllByOrderId(orderId);
            exchangeOrderDO.setDetail(details);
        });
        return new PageInfo<ExchangeOrderDO>(orders);
    }

    @Override
    public void cancelOrder(String orderId, BigDecimal tradedAmount, BigDecimal turnover) {

    }

    /**
     * 对发生交易的委托处理相应的钱包
     *
     * @param order               委托订单
     * @param trade               交易详情
     * @param coin                交易币种信息，包括手续费、交易币种信息等
     * @param secondReferrerAward 二级推荐人是否返佣
     */
    public void processOrder(ExchangeOrder order, ExchangeTrade trade, ExchangeCoin coin, boolean secondReferrerAward) {
        try {
            // 获取当前时间
            Long time = Calendar.getInstance().getTimeInMillis();
            // 创建订单详情
            ExchangeOrderDetail orderDetail = createOrderDetail(order, trade, time, coin);
            exchangeOrderDetailRepository.save(orderDetail);
            // 创建订单汇总
            OrderDetailAggregation aggregation = createAggregation(order, trade, orderDetail);
            orderDetailAggregationRepository.save(aggregation);
            // 计算可用收入币种数量
            BigDecimal incomeCoinAmount = calculateIncomeCoinAmount(order, trade, orderDetail);
            // 更新钱包余额
            updateWalletBalances(order, incomeCoinAmount, trade);

            // 保存收入交易记录
            saveTransaction(incomeCoinAmount, order, orderDetail, TransactionTypeEnum.EXCHANGE.getKey());
            // 保存支出交易记录
            saveOutcomeTransaction(order, trade, incomeCoinAmount);
            // 如果是卖出订单，则发放推广奖励
//            if (order.getDirection() == OrderDirectionEnum.SELL.getKey()) {
//                promoteReward(orderDetail.getFee(), memberService.findOne(order.getMemberId()), order.getDirection(), secondReferrerAward);
//            }
        } catch (Exception e) {
            log.error("处理交易明细时出错: {}", e.getMessage(), e);
        }
    }


    /**
     * 创建订单详情
     */
    private ExchangeOrderDetail createOrderDetail(ExchangeOrder order, ExchangeTrade trade, Long time, ExchangeCoin coin) {
        ExchangeOrderDetail orderDetail = new ExchangeOrderDetail();
        orderDetail.setOrderId(order.getOrderId());
        orderDetail.setTime(time);
        orderDetail.setPrice(trade.getPrice());
        orderDetail.setAmount(trade.getAmount());
        // 设置成交额
        String direction = order.getDirection();
        orderDetail.setTurnover(direction.equals(OrderDirectionEnum.BUY.getKey()) ? trade.getBuyTurnover() : trade.getSellTurnover());
        // 计算手续费
        orderDetail.setFee(calculateFee(order, trade, coin));
        return orderDetail;
    }

    // 计算手续费
    private BigDecimal calculateFee(ExchangeOrder order, ExchangeTrade trade, ExchangeCoin coin) {
        BigDecimal fee;
        String direction = order.getDirection();
        if (direction.equals(OrderDirectionEnum.BUY.getKey())) {
            fee = trade.getAmount().multiply(coin.getFee());
        } else {
            fee = trade.getSellTurnover().multiply(coin.getFee());
        }
        // 如果是机器人或超级管理员，则不收取手续费
        return (order.getMemberId() == 1 || order.getMemberId() == 10001) ? BigDecimal.ZERO : fee;
    }

    // 创建订单汇总
    private OrderDetailAggregation createAggregation(ExchangeOrder order, ExchangeTrade trade, ExchangeOrderDetail orderDetail) {
        OrderDetailAggregation aggregation = new OrderDetailAggregation();
        aggregation.setBizOrderType(BizOrderTypeEnum.EXCHANGE.getDescription());
        aggregation.setAmount(order.getAmount().doubleValue());
        aggregation.setFee(orderDetail.getFee().doubleValue());
        aggregation.setTime(orderDetail.getTime());
        aggregation.setDirection(order.getDirection());
        aggregation.setOrderId(order.getOrderId());
        String direction = order.getDirection();
        aggregation.setUnit(direction == OrderDirectionEnum.BUY.getKey() ? order.getBaseSymbol() : order.getCoinSymbol());
        Long memberId = order.getMemberId();
        Member member = getMemberById(memberId);
        // 获取会员信息
        if (member != null) {
            aggregation.setMemberId(member.getId());
            aggregation.setUsername(member.getUsername());
            aggregation.setRealName(member.getRealName());
        }
        return aggregation;
    }


    /**
     * 计算可收入的币种数量
     */
    private BigDecimal calculateIncomeCoinAmount(ExchangeOrder order, ExchangeTrade trade, ExchangeOrderDetail orderDetail) {
        String direction = order.getDirection();
        return direction == OrderDirectionEnum.BUY.getKey() ? trade.getAmount().subtract(orderDetail.getFee())
                : orderDetail.getTurnover().subtract(orderDetail.getFee());
    }

    // 更新钱包余额
    private void updateWalletBalances(ExchangeOrder order, BigDecimal incomeCoinAmount, ExchangeTrade trade) {
        String direction = order.getDirection();
        String incomeSymbol = direction.equals(OrderDirectionEnum.BUY.getKey()) ? order.getCoinSymbol() : order.getBaseSymbol();
        MemberWalletDO incomeWallet = memberWalletMapper.selectMemberWallet(incomeSymbol, order.getMemberId());
        memberWalletMapper.increaseBalance(incomeWallet.getId(), incomeCoinAmount);
        String outcomeSymbol = direction.equals(OrderDirectionEnum.BUY.getKey()) ? order.getBaseSymbol() : order.getCoinSymbol();
        BigDecimal outcomeCoinAmount = direction.equals(OrderDirectionEnum.BUY.getKey()) ? trade.getBuyTurnover() : trade.getAmount();
        MemberWalletDO outcomeWallet = memberWalletMapper.selectMemberWallet(outcomeSymbol, order.getMemberId());
        memberWalletMapper.decreaseFrozen(outcomeWallet.getId(), outcomeCoinAmount);
    }

    // 保存收入交易记录
    private void saveTransaction(BigDecimal incomeCoinAmount, ExchangeOrder order, ExchangeOrderDetail orderDetail, String type) {
        MemberTransaction transaction = new MemberTransaction();
        transaction.setAmount(incomeCoinAmount);
        String direction = order.getDirection();
        transaction.setSymbol(direction.equals(OrderDirectionEnum.BUY.getKey()) ? order.getCoinSymbol() : order.getBaseSymbol());
        transaction.setMemberId(order.getMemberId());
        transaction.setType(type);
        transaction.setFee(orderDetail.getFee());
        transaction.setDiscountFee(BigDecimal.ZERO);
        transaction.setRealFee(orderDetail.getFee());
        memberTransactionMapper.insert(transaction);
    }

    // 保存支出交易记录
    private void saveOutcomeTransaction(ExchangeOrder order, ExchangeTrade trade, BigDecimal incomeCoinAmount) {
        MemberTransaction transaction = new MemberTransaction();
        transaction.setAmount(incomeCoinAmount.negate());
        String direction = order.getDirection();
        transaction.setSymbol(direction.equals(OrderDirectionEnum.BUY.getKey()) ? order.getBaseSymbol() : order.getCoinSymbol());
        transaction.setMemberId(order.getMemberId());
        transaction.setType(TransactionTypeEnum.EXCHANGE.getKey());
        transaction.setFee(BigDecimal.ZERO);
        transaction.setRealFee(BigDecimal.ZERO);
        transaction.setDiscountFee(BigDecimal.ZERO);
        memberTransactionMapper.insert(transaction);
    }

    /**
     * 判断用户是否为机器人或管理员
     */
    private boolean isRobotOrAdmin(Long memberId) {
        return memberId == 1 || memberId == 10001; // 判断用户是否为机器人或超级管理员
    }


    /**
     * 根据会员 ID 获取会员信息
     */
    private Member getMemberById(Long memberId) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getId, memberId);
        return memberMapper.selectOne(queryWrapper);
    }
}
