package com.coopang.order.infrastructure.repository.paymenthistory;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import com.coopang.order.domain.repository.paymenthistory.PaymentHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistoryEntity, UUID>, PaymentHistoryRepository ,PaymentHistoryRepositoryCustom{
}
