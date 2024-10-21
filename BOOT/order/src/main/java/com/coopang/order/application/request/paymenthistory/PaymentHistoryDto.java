package com.coopang.order.application.request.paymenthistory;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentHistoryDto {

    private UUID orderId;
    private UUID pgPaymentId;
    private String paymentMethod;
    private BigDecimal paymentPrice;
}
