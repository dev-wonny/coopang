package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CompletePaymentHistory {
    private UUID orderId;
    private UUID pgPaymentId;
    private String paymentMethod;
    private BigDecimal paymentPrice;
}
