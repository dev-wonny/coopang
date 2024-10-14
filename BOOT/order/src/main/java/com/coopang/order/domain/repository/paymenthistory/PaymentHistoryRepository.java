package com.coopang.order.domain.repository.paymenthistory;

import com.coopang.order.domain.entity.paymenthistory.PaymentHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PaymentHistoryRepository {
    Optional<PaymentHistoryEntity> findById(UUID paymentHistoryId);

//    Page<PaymentHistoryEntity> findAllbyPaymentHistory(Pageable pageable);
//
//    Page<PaymentHistoryEntity> findAllbyPaymentHistorySearch(Pageable pageable);
}
