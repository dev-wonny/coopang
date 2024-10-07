package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProcessPayment {
    private UUID orderId;
    private UUID productId;
    private Integer orderQuantity;
    private BigDecimal orderTotalPrice;
}
