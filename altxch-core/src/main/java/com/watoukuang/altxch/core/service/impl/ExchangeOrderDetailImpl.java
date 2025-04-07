package com.watoukuang.altxch.core.service.impl;

import com.watoukuang.altxch.core.dao.repository.ExchangeOrderDetailRepository;
import com.watoukuang.altxch.core.domain.ExchangeOrderDetail;
import com.watoukuang.altxch.core.service.ExchangeOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeOrderDetailImpl implements ExchangeOrderDetailService {
    private final ExchangeOrderDetailRepository orderDetailRepository;

    @Override
    public List<ExchangeOrderDetail> findAllByOrderId(String orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }
}
