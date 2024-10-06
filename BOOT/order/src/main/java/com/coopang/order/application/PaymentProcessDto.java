package com.coopang.order.application;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentProcessDto {
    private UUID orderId;
    private UUID productId;
    private Integer orderQuantity;
    private BigDecimal orderTotalPrice;
}
