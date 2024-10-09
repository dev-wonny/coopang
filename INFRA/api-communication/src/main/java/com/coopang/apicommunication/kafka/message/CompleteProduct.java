package com.coopang.apicommunication.kafka.message;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompleteProduct {
    private UUID orderId;
    private UUID companyId;
    private Integer orderQuantity;
    private BigDecimal orderTotalPrice;
}
