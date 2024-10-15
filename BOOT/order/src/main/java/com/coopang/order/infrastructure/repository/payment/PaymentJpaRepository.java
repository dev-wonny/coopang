package com.coopang.order.infrastructure.repository.payment;

import com.coopang.order.domain.entity.payment.PaymentEntity;
import com.coopang.order.domain.repository.payment.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID>, PaymentRepository {
}
