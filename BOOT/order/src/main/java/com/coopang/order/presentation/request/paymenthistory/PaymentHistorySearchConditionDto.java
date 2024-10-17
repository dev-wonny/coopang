package com.coopang.order.presentation.request.paymenthistory;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PaymentHistorySearchConditionDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID orderId;
}
