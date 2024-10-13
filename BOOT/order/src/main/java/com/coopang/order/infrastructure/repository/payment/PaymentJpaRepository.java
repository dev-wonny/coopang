package com.coopang.order.infrastructure.repository.payment;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import com.coopang.order.domain.repository.paymenthistory.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentHistoryEntity, UUID>, PaymentRepository {
}
