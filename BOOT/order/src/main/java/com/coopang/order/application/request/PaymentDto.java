package com.coopang.order.application.request;

import com.coopang.order.domain.PaymentMethodEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentDto {

    private UUID orderId;
    private PaymentMethodEnum paymentMethod;
    private BigDecimal paymentPrice;

}
