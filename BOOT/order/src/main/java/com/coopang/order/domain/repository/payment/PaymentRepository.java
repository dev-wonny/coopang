package com.coopang.order.domain.repository.payment;

import com.coopang.order.domain.entity.payment.PaymentEntity;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Optional<PaymentEntity> findByOrderId(UUID orderId);
}
