package com.coopang.order.domain.repository.paymenthistory;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Optional<PaymentHistoryEntity> findByOrderId(UUID orderId);
}
