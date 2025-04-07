package com.watoukuang.altxch.core.dao.repository;

import com.watoukuang.altxch.core.domain.OrderDetailAggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderDetailAggregationRepository extends MongoRepository<OrderDetailAggregation, String> {
}
