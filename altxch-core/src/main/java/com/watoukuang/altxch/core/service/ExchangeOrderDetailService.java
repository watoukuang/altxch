package com.watoukuang.altxch.core.service;

import com.watoukuang.altxch.core.domain.ExchangeOrderDetail;

import java.util.List;

public interface ExchangeOrderDetailService {
    List<ExchangeOrderDetail> findAllByOrderId(String orderId);
}
