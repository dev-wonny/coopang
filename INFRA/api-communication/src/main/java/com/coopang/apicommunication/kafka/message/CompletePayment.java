package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CompletePayment {
    private UUID orderId;
    private UUID pgPaymentId;
    private BigDecimal orderTotalPrice;
    private String paymentMethod;
}
