package com.coopang.apicommunication.kafka.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessProdcut {
    private UUID orderId;
    private UUID productId;
    private Integer orderQuantity;
    private Integer orderTotalPrice;
}
