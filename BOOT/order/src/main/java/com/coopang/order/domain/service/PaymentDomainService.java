package com.coopang.order.domain.service;

import com.coopang.order.application.PaymentProcessDto;
import com.coopang.order.domain.PaymentStatusEnum;
import com.coopang.order.domain.entity.payment.PaymentEntity;
import com.coopang.order.infrastructure.repository.PaymentJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentDomainService")
@Service
@Transactional
public class PaymentDomainService {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentDomainService(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    public void createPayment(PaymentProcessDto paymentProcessDto,PaymentStatusEnum paymentStatus ){
        PaymentEntity paymentEntity = PaymentEntity.create(
                paymentProcessDto.getOrderId(),
                paymentProcessDto.getOrderTotalPrice(),
                paymentStatus
        );
        paymentJpaRepository.save(paymentEntity);
    }
}
