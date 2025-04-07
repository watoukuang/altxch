package com.watoukuang.altxch.core.dao.repository;

import com.watoukuang.altxch.core.domain.ExchangeOrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExchangeOrderDetailRepository extends MongoRepository<ExchangeOrderDetail, String> {
    List<ExchangeOrderDetail> findAllByOrderId(String orderId);

}
