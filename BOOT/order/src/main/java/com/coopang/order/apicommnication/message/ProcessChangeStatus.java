package com.coopang.order.apicommnication.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessChangeStatus {
    private UUID orderId;
    private String deliveryStatus;
}

