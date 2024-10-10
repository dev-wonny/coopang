package com.coopang.order.application.request.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentDto {

    private UUID orderId;
    private String paymentMethod;
    private BigDecimal paymentPrice;

}
