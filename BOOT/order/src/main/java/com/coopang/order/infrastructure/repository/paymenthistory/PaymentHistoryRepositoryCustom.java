package com.coopang.order.infrastructure.repository.paymenthistory;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import com.coopang.order.presentation.request.paymenthistory.PaymentHistorySearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentHistoryRepositoryCustom {
    Page<PaymentHistoryEntity> search(PaymentHistorySearchConditionDto paymentHistorySearchConditionDto, Pageable pageable);
    Page<PaymentHistoryEntity> findAll(PaymentHistorySearchConditionDto paymentHistorySearchConditionDto, Pageable pageable);
}
